package com.kakao.kakaolink.v4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.kakao.kakaolink.R;
import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.kakaolink.v4.network.KakaoLinkTemplateRequest;
import com.kakao.kakaolink.v4.network.TemplateScrapRequest;
import com.kakao.kakaolink.v4.network.TemplateScrapResponse;
import com.kakao.kakaolink.v4.network.TemplateValidateRequest;
import com.kakao.kakaolink.v4.network.KakaoLinkTemplateResponse;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

/**
 * 실제로 Template validate을 하고 Intent를 생성하여 카카오링크 4.0 메시지를 보내는 클래스.
 * @author kevin.kang
 * Created by kevin.kang on 2016. 11. 28..
 */

class KakaoLinkSender {
    private KakaoTaskQueue taskQueue;
    private KakaoLinkApi linkApi;

    KakaoLinkSender(final KakaoTaskQueue taskQueue, final KakaoLinkApi linkApi) {
        this.taskQueue = taskQueue;
        this.linkApi = linkApi;
    }

    void send(final Context context, final TemplateValidateRequest request, final ResponseCallback<KakaoLinkResponse> callback) {
        taskQueue.addTask(getKakaoLinkResultTask(context, request, callback));
    }

    void sendUrl(final Context context, final TemplateScrapRequest request, final ResponseCallback<KakaoLinkResponse> callback) {
        taskQueue.addTask(getKakaoLinkResultTask(context, request, callback));
    }

    @SuppressWarnings("WeakerAccess")
    KakaoResultTask<KakaoLinkResponse> getKakaoLinkResultTask(final Context context, final KakaoLinkTemplateRequest request, final ResponseCallback<KakaoLinkResponse> callback) {
        return new KakaoResultTask<KakaoLinkResponse>(callback) {
            @Override
            public KakaoLinkResponse call() throws Exception {
                KakaoLinkTemplateResponse response = null;
                if (request instanceof TemplateValidateRequest) {
                    response = linkApi.requestTemplateValidate((TemplateValidateRequest)request);
                } else {
                    response = linkApi.requestTemplateScrap((TemplateScrapRequest)request);
                }

                Intent intent;
                try {
                    intent = createKakaoLinkIntent(request, response);
                    context.startActivity(intent);
                    return new KakaoLinkResponse(response.getTemplateMsg(), response.getWarningMsg());
                } catch (final KakaoException e) {
                    if (e.getErrorType() == KakaoException.ErrorType.URI_LENGTH_EXCEEDED) {
                        final KakaoException eWithMessage = new KakaoException(KakaoException.ErrorType.URI_LENGTH_EXCEEDED, context.getResources().getString(R.string.com_kakao_alert_uri_too_long));
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                getUriLengthExceededAlertDialog(context, eWithMessage, callback).show();
                            }
                        });
                        throw eWithMessage;
                    } else {
                        throw new KakaoException(KakaoException.ErrorType.UNSPECIFIED_ERROR);
                    }
                }
            }
        };
    }

    Intent createKakaoLinkIntent(final KakaoLinkTemplateRequest request, final KakaoLinkTemplateResponse response) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(KakaoTalkLinkProtocol.LINK_SCHEME).authority(KakaoTalkLinkProtocol.LINK_AUTHORITY);
        builder.appendQueryParameter(KakaoTalkLinkProtocol.LINKVER, KakaoTalkLinkProtocol.LINK_VERSION_40);
        builder.appendQueryParameter(KakaoTalkLinkProtocol.APP_KEY, request.getAppKey());
        builder.appendQueryParameter(KakaoTalkLinkProtocol.APP_VER, request.getAppVer());


        String templateId;
        String templateArgsString = null;

        if (response instanceof TemplateScrapResponse) {
            templateId = ((TemplateScrapResponse) response).getTemplateId();
        } else {
            templateId = request.getTemplateId();
        }
        if (templateId != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ID, templateId);
        }

        if (response instanceof TemplateScrapResponse) {
            JSONObject templateArgs = ((TemplateScrapResponse) response).getTemplateArgs();
            if (templateArgs != null) {
                templateArgsString = templateArgs.toString();
            }
        } else  {
            templateArgsString = request.getTemplateArgsString();
        }
        if (templateArgsString != null) {
            builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_ARGS, templateArgsString);
        }

        builder.appendQueryParameter(KakaoTalkLinkProtocol.TEMPLATE_JSON, response.getTemplateMsg().toString());
        Uri uri = builder.build();
        if (uri.toString().length() > KakaoTalkLinkProtocol.LINK_URI_MAX_SIZE) {
            throw new KakaoException(KakaoException.ErrorType.URI_LENGTH_EXCEEDED);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @SuppressWarnings("WeakerAccess")
    AlertDialog getUriLengthExceededAlertDialog(final Context context, final KakaoException e, final ResponseCallback<KakaoLinkResponse> callback) {
        String message = context.getResources().getString(R.string.com_kakao_alert_uri_too_long);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                callback.onFailure(new ErrorResult(new IllegalArgumentException(e.getMessage())));
            }
        });
        return dialog;
    }
}