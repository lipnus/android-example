/**
 * Copyright 2014-2016 Kakao Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.auth.authorization.accesstoken;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import com.kakao.auth.KakaoSDK;
import com.kakao.auth.StringSet;
import com.kakao.util.helper.SharedPreferencesCache;
import com.kakao.auth.network.response.AuthResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseBody.ResponseBodyException;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * refresh token에 대한 expires_at은 아직 내려오지 않는다.
 * @author MJ
 */
public class AccessToken extends AuthResponse {
    private static final String CACHE_ACCESS_TOKEN = "com.kakao.token.AccessToken";
    private static final String CACHE_ACCESS_TOKEN_EXPIRES_AT = "com.kakao.token.AccessToken.ExpiresAt";
    private static final String CACHE_REFRESH_TOKEN = "com.kakao.token.RefreshToken";
    private static final String CACHE_REFRESH_TOKEN_EXPIRES_AT = "com.kakao.token.RefreshToken.ExpiresAt";
    private static final String CACHE_KAKAO_SECURE_MODE = "com.kakao.token.KakaoSecureMode";
    private static final Date MIN_DATE = new Date(Long.MIN_VALUE);
    private static final Date MAX_DATE = new Date(Long.MAX_VALUE);
    private static final Date DEFAULT_EXPIRATION_TIME = MAX_DATE;
    private static final Date ALREADY_EXPIRED_EXPIRATION_TIME = MIN_DATE;

    private static AESEncryptor encryptor;

    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiresAt;
    private Date refreshTokenExpiresAt;

    public static AccessToken createEmptyToken() {
        return new AccessToken("", "", ALREADY_EXPIRED_EXPIRATION_TIME, ALREADY_EXPIRED_EXPIRATION_TIME);
    }

    private static boolean getLastSecureMode(final SharedPreferencesCache cache) {
        String lastSecureModeString = cache.getString(CACHE_KAKAO_SECURE_MODE);
        return lastSecureModeString != null && lastSecureModeString.equals("true");
    }

    private static AESEncryptor getEncryptor(final Context context) throws GeneralSecurityException {
        if (encryptor == null) {
            byte[] salt;
            try {
                salt = AndroidIdUtils.generateAndroidId(context);
            } catch (Exception e) {
                salt = ("xxxx" + Build.PRODUCT + "a23456789012345bcdefg").getBytes();
            }
            encryptor = new AESEncryptor(context, salt);
        }
        return encryptor;
    }

    private static String decrypt(final Context context, final String cipher) throws GeneralSecurityException, IOException {
        return getEncryptor(context).decrypt(cipher);
    }

    private static String encrypt(final Context context, final String plain) throws GeneralSecurityException, IOException {
        return getEncryptor(context).encrypt(plain);
    }

    public static AccessToken createFromCache(final Context context, final SharedPreferencesCache cache) {

        // accessToken과 refreshToken을 가져옴. 암호화되어 있을수도 있고 안 되어 있을수도 있다.
        String accessToken = cache.getString(CACHE_ACCESS_TOKEN);
        String refreshToken = cache.getString(CACHE_REFRESH_TOKEN);
        String encryptedAccessToken;
        String encryptedRefreshToken;

        boolean lastSecureMode = getLastSecureMode(cache);
        boolean currentSecureMode = KakaoSDK.getAdapter().getSessionConfig().isSecureMode();

        try {
            if (lastSecureMode) {
                // 암호화되어 있었다면 decrypt한 값을 accessToken과 refreshToken에 저장.
                encryptedAccessToken = accessToken;
                encryptedRefreshToken = refreshToken;
                if (encryptedAccessToken != null) {
                    accessToken = decrypt(context, encryptedAccessToken);
                }
                if (encryptedRefreshToken != null) {
                    refreshToken = decrypt(context, encryptedRefreshToken);
                }
            }

            // KAKAO_SECURE_MODE가 바뀌었다면 token 암호화/해독한다.
            if (lastSecureMode != currentSecureMode) {
                Bundle bundle = new Bundle();
                if (currentSecureMode) {
                    // KAKAO_SECURE_MODE가 OFF에서 ON으로 변경됨. 토큰을 암호화서 저장.
                    if (accessToken != null) {
                        encryptedAccessToken = encrypt(context, accessToken);
                        bundle.putString(CACHE_ACCESS_TOKEN, encryptedAccessToken);
                    }
                    if (refreshToken != null) {
                        encryptedRefreshToken = encrypt(context, refreshToken);
                        bundle.putString(CACHE_REFRESH_TOKEN, encryptedRefreshToken);
                    }
                } else {
                    // KAKAO_SECURE_MODE가 ON에서 OFF로 변경됨. 해독하여 저장.
                    if (accessToken != null) {
                        bundle.putString(CACHE_ACCESS_TOKEN, accessToken);
                    }
                    if (refreshToken != null) {
                        bundle.putString(CACHE_REFRESH_TOKEN, refreshToken);
                    }
                }
                bundle.putString(CACHE_KAKAO_SECURE_MODE, String.valueOf(currentSecureMode));
                cache.save(bundle);
            }
        } catch (Exception e) {
            // 암호화 도중 예외가 발생하면 로그인을 풀어야 한다.
            accessToken = null;
            refreshToken = null;
            e.printStackTrace();
        }

        final Date accessTokenExpiresAt = cache.getDate(CACHE_ACCESS_TOKEN_EXPIRES_AT);
        final Date refreshTokenExpiresAt = cache.getDate(CACHE_REFRESH_TOKEN_EXPIRES_AT);
        return new AccessToken(accessToken, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt);
    }

    public AccessToken(ResponseBody body) throws ResponseBodyException, AuthResponseStatusError {
        super(body);
        if (!body.has(StringSet.access_token)) {
            Logger.e("");
            throw new ResponseBody.ResponseBodyException("No Search Element : " + StringSet.access_token);
        }
        accessToken = body.getString(StringSet.access_token);
        if (body.has(StringSet.refresh_token)) {
            refreshToken = body.getString(StringSet.refresh_token);
        }
        long expiredAt = new Date().getTime() + body.getInt(StringSet.expires_in) * 1000;
        accessTokenExpiresAt = new Date(expiredAt);
        refreshTokenExpiresAt = MAX_DATE;
    }

    private AccessToken(final String accessToken, final String refreshToken, final Date accessTokenExpiresAt, final Date refreshTokenExpiresAt) {
        super();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public void clearAccessToken(final SharedPreferencesCache cache) {
        this.accessToken = null;
        this.accessTokenExpiresAt = DEFAULT_EXPIRATION_TIME;
        final List<String> keysToRemove = new ArrayList<String>();
        keysToRemove.add(CACHE_ACCESS_TOKEN);
        keysToRemove.add(CACHE_ACCESS_TOKEN_EXPIRES_AT);
        cache.clear(keysToRemove);
    }

    public void clearRefreshToken(final SharedPreferencesCache cache) {
        this.refreshToken = null;
        this.refreshTokenExpiresAt = DEFAULT_EXPIRATION_TIME;
        final List<String> keysToRemove = new ArrayList<String>();
        keysToRemove.add(CACHE_REFRESH_TOKEN);
        keysToRemove.add(CACHE_REFRESH_TOKEN_EXPIRES_AT);
        cache.clear(keysToRemove);
    }

    public void removeAccessTokenToCache(final SharedPreferencesCache cache) {
        final List<String> keysToRemove = new ArrayList<String>();
        keysToRemove.add(CACHE_ACCESS_TOKEN);
        keysToRemove.add(CACHE_ACCESS_TOKEN_EXPIRES_AT);
        cache.clear(keysToRemove);
    }

    public void saveAccessTokenToCache(final Context context, final SharedPreferencesCache cache) {
        Bundle bundle = new Bundle();
        // encrypt accessToken and refreshToken here
        if (KakaoSDK.getAdapter().getSessionConfig().isSecureMode()) {
            try {
                String encryptedAccessToken = encrypt(context, accessToken);
                String encryptedRefreshToken = encrypt(context, refreshToken);
                bundle.putString(CACHE_ACCESS_TOKEN, encryptedAccessToken);
                bundle.putString(CACHE_REFRESH_TOKEN, encryptedRefreshToken);
            } catch (Exception e) {
                bundle.putString(CACHE_ACCESS_TOKEN, null);
                bundle.putString(CACHE_REFRESH_TOKEN, null);
                e.printStackTrace();
            }
        } else {
            bundle.putString(CACHE_ACCESS_TOKEN, accessToken);
            bundle.putString(CACHE_REFRESH_TOKEN, refreshToken);
        }

        bundle.putLong(CACHE_ACCESS_TOKEN_EXPIRES_AT, accessTokenExpiresAt.getTime());
        bundle.putLong(CACHE_REFRESH_TOKEN_EXPIRES_AT, refreshTokenExpiresAt.getTime());
        bundle.putString(CACHE_KAKAO_SECURE_MODE, String.valueOf(KakaoSDK.getAdapter().getSessionConfig().isSecureMode()));
        cache.save(bundle);
    }

    // access token 갱신시에는 refresh token이 내려오지 않을 수도 있다.
    public void updateAccessToken(final AccessToken newAccessToken){
        String newRefreshToken = newAccessToken.refreshToken;
        if(TextUtils.isEmpty(newRefreshToken)){
            this.accessToken = newAccessToken.accessToken;
            this.accessTokenExpiresAt = newAccessToken.accessTokenExpiresAt;
        } else {
            this.accessToken = newAccessToken.accessToken;
            this.refreshToken = newAccessToken.refreshToken;
            this.accessTokenExpiresAt = newAccessToken.accessTokenExpiresAt;
            this.refreshTokenExpiresAt = newAccessToken.refreshTokenExpiresAt;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean hasRefreshToken(){
        return !Utility.isNullOrEmpty(this.refreshToken);
    }

    public boolean hasValidAccessToken() {
        return !Utility.isNullOrEmpty(this.accessToken) && !new Date().after(this.accessTokenExpiresAt);
    }

    public int getRemainedExpiresInAccessTokenTime() {
        if (accessTokenExpiresAt == null || !hasValidAccessToken()) {
            return 0;
        }

        return (int) (accessTokenExpiresAt.getTime() - new Date().getTime());
    }

    /**
     * AES 알고리즘을 사용하는 Encryptor 클래스.
     * reference: https://github.daumkakao.com/api-dev/kakao-api-android-sdk/blob/master/src/com/kakao/api/SecureStorage.java
     */
    private static class AESEncryptor {
        private static final byte[] initVector = {
                112, 78, 75, 55, -54, -30, -10, 44, 102, -126, -126, 92, -116, -48, -123, -55
        };
        private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(initVector);

        private static final String keyGenAlgorithm = Utils.base64DecodeAndXor("My0oeSI1IzInbyA+LVFaW2wiNSokPAMiMipOLS4=");
        private static final String cipherAlgorithm = Utils.base64DecodeAndXor("Iio+ASgjKE4/ZSIjXDMOCUoCDww=");
        private static final String algorithm = "AES";
        private static final int ITER_COUNT = 2;
        private static final int KEY_LENGTH = 256;
        private static final String CHAR_SET = "UTF-8";

        private Cipher encryptor;
        private Cipher decryptor;

        AESEncryptor(final Context context, final byte[] salt) throws GeneralSecurityException {
            String keyValue = Utility.getKeyHash(context);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(keyGenAlgorithm);
            KeySpec keySpec = new PBEKeySpec(keyValue.substring(0, Math.min(keyValue.length(), 16)).toCharArray(), salt, ITER_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), algorithm);

            encryptor = Cipher.getInstance(cipherAlgorithm);
            decryptor = Cipher.getInstance(cipherAlgorithm);

            try {
                encryptor.init(Cipher.ENCRYPT_MODE, secret, IV_PARAMETER_SPEC);
                decryptor.init(Cipher.DECRYPT_MODE, secret, IV_PARAMETER_SPEC);
            } catch (InvalidKeyException e) {
                // Due to invalid key size. Using 128 bits instead.
                SecretKey shorterSecret = new SecretKeySpec(Arrays.copyOfRange(tmp.getEncoded(), 0, tmp.getEncoded().length / 2), algorithm);
                encryptor.init(Cipher.ENCRYPT_MODE, shorterSecret, IV_PARAMETER_SPEC);
                decryptor.init(Cipher.DECRYPT_MODE, shorterSecret, IV_PARAMETER_SPEC);
            }
        }

        /**
         * @param value 암호화 하고자 하는 스트링
         * @return 암호화된 스트링. 예외가 발생하게 되면 null을 리턴한다.
         */
        String encrypt(String value) throws GeneralSecurityException, IOException {
            byte[] encrypted = encryptor.doFinal(value.getBytes(CHAR_SET));
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        }

        /**
         * @param encrypted 해독하고자 하는 암호화된 스트링
         * @return 해독된 스트링. 예외가 발생하게 되면 null 리턴.
         */
        String decrypt(String encrypted) throws GeneralSecurityException, IOException {
            byte[] original = decryptor.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(original, CHAR_SET);
        }
    }

    /**
     * Device 고유의 UUID를 만드는 클래스
     * reference: https://github.daumkakao.com/api-dev/kakao-api-android-sdk/blob/master/src/com/kakao/api/SecureStorage.java
     */
    private static class AndroidIdUtils {
        private static final String DIGEST_ALGORITHM = "SHA-256";

        static byte[] generateAndroidId(Context context) throws NoSuchAlgorithmException {
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (androidId == null) throw new NullPointerException("android_id is null.");
            androidId = stripZeroOrSpace(androidId);
            androidId = String.format("SDK-%s", androidId);
            return hash(androidId);
        }

        private static String stripZeroOrSpace(String str) {
            return str == null ? null : str.replaceAll("[0\\s]", "");
        }

        private static byte[] hash(String uuid) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.reset();
            md.update(uuid.getBytes());

            return md.digest();
        }
    }

    private static class Utils {
        static String xorMessage(String message) {
            return xorMessage(message, "com.kakao.api");
        }

        static String xorMessage(String message, String key) {
            try {
                if (message == null || key == null) {
                    return null;
                }

                char[] keys = key.toCharArray();
                char[] msg = message.toCharArray();

                int ml = msg.length;
                int kl = keys.length;
                char[] newMsg = new char[ml];

                for (int i = 0; i < ml; i++) {
                    newMsg[i] = (char) (msg[i] ^ keys[i % kl]);
                }
                return new String(newMsg);
            } catch (Exception e) {
                return null;
            }
        }

        static String base64DecodeAndXor(String source) {
            return xorMessage(new String(Base64.decode(source, Base64.DEFAULT)));
        }
    }
}
