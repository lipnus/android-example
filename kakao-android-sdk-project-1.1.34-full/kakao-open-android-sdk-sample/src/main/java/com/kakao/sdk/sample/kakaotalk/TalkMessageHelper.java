package com.kakao.sdk.sample.kakaotalk;

import android.content.Context;
import android.content.DialogInterface;

import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.widget.DialogBuilder;
import com.kakao.sdk.sample.friends.FriendsMainActivity.MSG_TYPE;
import com.kakao.util.helper.log.Logger.DeployPhase;

/**
 * @author leoshin on 15. 9. 4.
 */
class TalkMessageHelper {
    static String getMemoTemplateId() {
        switch (DeployPhase.current()) {
            case Local:
            case Alpha:
                return "3058";
            case Sandbox:
                return "21";
            case Beta:
            case Release:
                return "267";
            default:
                return null;
        }
    }

    static String getSampleTemplateId(MSG_TYPE msgType) {
        switch (DeployPhase.current()) {
            case Local:
            case Alpha:
                return getAlphaTemplateId(msgType);
            case Sandbox:
                return getSandboxTemplateId(msgType);
            case Beta:
            case Release:
                return getReleaseTemplateId(msgType);
            default:
                return null;
        }
    }

    static String getAlphaTemplateId(MSG_TYPE msgType) {
        switch (msgType) {
            case INVITE:
                return "2180";
            case FEED:
                return "2179";
            default:
                return "2181";
        }
    }

    static String getSandboxTemplateId(MSG_TYPE msgType) {
        switch (msgType) {
            case INVITE:
                return "19";
            case FEED:
                return "20  ";
            default:
                return "18";
        }
    }

    static String getReleaseTemplateId(MSG_TYPE msgType) {
        switch (msgType) {
            case INVITE:
                return "152";
            case FEED:
                return "151";
            default:
                return "153";
        }
    }

    static void showSendMessageDialog(Context context, final DialogInterface.OnClickListener listener) {
        final String message = context.getString(R.string.send_message);
        new DialogBuilder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
