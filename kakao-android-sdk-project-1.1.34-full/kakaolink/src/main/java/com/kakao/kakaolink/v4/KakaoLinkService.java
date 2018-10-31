package com.kakao.kakaolink.v4;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.kakao.kakaolink.R;
import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.kakaolink.v4.network.TemplateScrapRequest;
import com.kakao.kakaolink.v4.network.TemplateValidateRequest;
import com.kakao.network.ErrorResult;
import com.kakao.network.NetworkTask;
import com.kakao.network.RequestConfiguration;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.tasks.KakaoTaskQueue;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.TalkProtocol;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 카카오링크 4.0 API를 사용하기 위한 클래스.
 * @author kevin.kang
 * Created by kevin.kang on 2016. 11. 25..
 */

public class KakaoLinkService {
    private static KakaoLinkService instance;

    /**
     * 카카오링크 4.0 API를 사용하기 위한 singleton 객체를 얻는다.
     * @return 카카오링크 4.0 API를 사용하기 위한 KakaoLinkService 객체
     */
    public static KakaoLinkService getInstance() {
        if (instance == null) {
            synchronized (KakaoLinkService.class) {
                if (instance == null) {
                    instance = new KakaoLinkService();
                }
            }
        }
        return instance;
    }

    /**
     * 카카오링크 4.0 메시지를 보내기 위해 카카오톡을 호출하는 메소드. 개발자 사이트에 등록한 template의 id와 template에 정의된 parameter들의 값을 제공해야 한다.
     * App key와 key hash가 정의되어 있지 않으면 이 메소드는 IllegalStateException을 던진다.
     * @param context 카카오톡을 실행시킬 context
     * @param templateId 개발자 웹사이트에서 등록한 template의 id
     * @param templateArgs template에 정의된 파라미터들과 그 파라미터들의 값의 쌍들로 이루어진 map
     * @param callback 성공/실패 여부와 각 케이스의 상세 정보를 전달받기 위한 callback
     */
    public void send(final Context context, final String templateId, final Map<String, String> templateArgs, final ResponseCallback<KakaoLinkResponse> callback) {
        if (context == null) {
            callback.onFailure(new ErrorResult(new NullPointerException("Context cannot be null.")));
            return;
        }

        // App key, key hash, KA header 등을 준비한다.
        final RequestConfiguration requestConfiguration = getRequestConfiguration(context);

        // 카카오링크 4.0을 실행시킬 수 있는 카카오톡 버전(6.0) 인지 체크한다.
        if (!isKakaoLink40Available(context)) {
            // 아닐 경우 AlertDialog를 띄운 후, 확인버튼을 누를 때 마켓으로 이동.
            getTalkInstallAlertDialog(context, requestConfiguration, callback).show();
            return;
        }

        final KakaoLinkSender sender = getKakaoLinkSender();
        try {
            KakaoLinkMessage message = new KakaoLinkMessage(templateId, templateArgs);
            sender.send(context, getTemplateValidateRequest(requestConfiguration, message), callback);
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure(new ErrorResult(e));
            }
        }
    }


    public void sendUrl(final Context context, final String url, final ResponseCallback<KakaoLinkResponse> callback) {
        sendUrl(context, url, null, null, callback);
    }

    public void sendUrl(final Context context, final String url, final String templateId, final Map<String, String> templateArgs, final ResponseCallback<KakaoLinkResponse> callback) {
        // App key, key hash, KA header 등을 준비한다.
        final RequestConfiguration requestConfiguration = getRequestConfiguration(context);

        // 카카오링크 4.0을 실행시킬 수 있는 카카오톡 버전(6.0) 인지 체크한다.
        if (!isKakaoLink40Available(context)) {
            // 아닐 경우 AlertDialog를 띄운 후, 확인버튼을 누를 때 마켓으로 이동.
            getTalkInstallAlertDialog(context, requestConfiguration, callback).show();
            return;
        }

        final KakaoLinkSender sender = getKakaoLinkSender();
        try {
            sender.sendUrl(context, getTemplateScrapRequest(requestConfiguration, url, templateId, templateArgs), callback);
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure(new ErrorResult(e));
            }
        }
    }


    KakaoLinkSender getKakaoLinkSender() {
       return new KakaoLinkSender(getTaskQueue(), getKakaoLinkApi(getNetworkTask()));
    }

    @SuppressWarnings("WeakerAccess")
    TemplateValidateRequest getTemplateValidateRequest(final RequestConfiguration configuration, final KakaoLinkMessage message) {
        return new TemplateValidateRequest(configuration, message.getTempalteId(), message.getTemplateArgs());
    }

    TemplateScrapRequest getTemplateScrapRequest(final RequestConfiguration configuration, final String url, final String templateId, final Map<String, String> templateArgs) {
        return new TemplateScrapRequest(configuration, url, templateId, templateArgs);
    }

    NetworkTask getNetworkTask() {
        return new NetworkTask();
    }

    KakaoTaskQueue getTaskQueue() {
        return KakaoTaskQueue.getInstance();
    }

    KakaoLinkApi getKakaoLinkApi(final NetworkTask networkTask) {
        return new KakaoLinkApi(networkTask);
    }

    RequestConfiguration getRequestConfiguration(final Context context) {
        return RequestConfiguration.createRequestConfiguration(context);
    }

    boolean isKakaoLink40Available(final Context context) {
        Uri uri = new Uri.Builder().scheme(KakaoTalkLinkProtocol.LINK_SCHEME).authority(KakaoTalkLinkProtocol.LINK_AUTHORITY).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return TalkProtocol.isKakaoLink40Available(context, intent);
    }

    @SuppressWarnings("WeakerAccess")
    String getReferrer(final RequestConfiguration configuration) {
        JSONObject json = new JSONObject();
        try {
            json.put(CommonProtocol.KA_HEADER_KEY, configuration.getKaHeader());
            json.put(KakaoTalkLinkProtocol.APP_KEY, configuration.getAppKey());
            json.put(KakaoTalkLinkProtocol.APP_VER, configuration.getAppVer());
            json.put(KakaoTalkLinkProtocol.APP_PACKAGE, configuration.getPackageName());
        } catch (JSONException e) {
            Logger.w(e);
            return "";
        }
        return json.toString();
    }

    AlertDialog getTalkInstallAlertDialog(final Context context, final RequestConfiguration requestConfiguration, final ResponseCallback<KakaoLinkResponse> callback) {
       AlertDialog dialog =  new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(context.getString(R.string.com_kakao_alert_install_kakaotalk))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent marketIntent;
                        try {
                            marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(KakaoTalkLinkProtocol.TALK_MARKET_URL_PREFIX + getReferrer(requestConfiguration)));
                            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(marketIntent);
                        } catch (ActivityNotFoundException e) {
                            marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(KakaoTalkLinkProtocol.TALK_MARKET_URL_PREFIX_2 + getReferrer(requestConfiguration)));
                            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(marketIntent);
                        }
                    }
                }).create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                callback.onFailure(new ErrorResult(new IllegalStateException(context.getString(R.string.com_kakao_alert_install_kakaotalk))));
            }
        });
        return dialog;
    }
}
