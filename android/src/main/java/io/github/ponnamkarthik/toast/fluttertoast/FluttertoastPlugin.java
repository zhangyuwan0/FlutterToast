package io.github.ponnamkarthik.toast.fluttertoast;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FluttertoastPlugin */
public class FluttertoastPlugin implements MethodCallHandler {

    private Context ctx;
    private Toast toast;

    private FluttertoastPlugin(Context context) {
        ctx = context;
    }

    /** Plugin registration. */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "PonnamKarthik/fluttertoast");
        channel.setMethodCallHandler(new FluttertoastPlugin(registrar.context()));
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        switch (call.method) {
            case "showToast":
                String msg = call.argument("msg").toString();
                String length = call.argument("length").toString();
                String gravity = call.argument("gravity").toString();
                Number bgcolor = call.argument("bgcolor");
                Number textcolor = call.argument("textcolor");
                Number textSize = call.argument("fontSize");

                Integer realGravity;
                switch (gravity) {
                    case "top":
                        realGravity = Gravity.TOP;
                        break;
                    case "center":
                        realGravity = Gravity.CENTER;
                        break;
                    default:
                        realGravity = Gravity.BOTTOM;
                }

                if(bgcolor != null && textcolor != null && textSize != null) {
                    LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.toast_custom, null);

                    TextView text = layout.findViewById(R.id.text);

                    toast = new Toast(ctx);


                    if (length.equals("long")) {
                        toast.setDuration(Toast.LENGTH_LONG);
                    } else {
                        toast.setDuration(Toast.LENGTH_SHORT);
                    }

                    text.setText(msg);

                    toast.setView(layout);

                    if (textSize != null)
                        text.setTextSize(textSize.floatValue());


                    Drawable shapeDrawable;

//                shapeDrawable = ctx.getDrawable(ctx.getResources(), R.drawable.toast_bg, null);

                    shapeDrawable  = ctx.getResources().getDrawable(R.drawable.toast_bg);

                    if (bgcolor != null && shapeDrawable != null) {
                        shapeDrawable.setColorFilter(bgcolor.intValue(), PorterDuff.Mode.SRC_IN);
                    }

                    layout.setBackground(shapeDrawable);

//            if(bgcolor != null) {
//                Drawable shapeDrawable;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    shapeDrawable = ctx.getResources().getDrawable(R.drawable.toast_bg,ctx.getTheme());
//                } else {
//                    shapeDrawable = ctx.getResources().getDrawable(R.drawable.toast_bg);
//                }
//
//                if (shapeDrawable != null) {
//                    shapeDrawable.setColorFilter(bgcolor.intValue(), PorterDuff.Mode.SRC_IN);
//                    if (Build.VERSION.SDK_INT <= 27) {
//                        toast.getView().setBackground(shapeDrawable);
//                    } else {
//                        toast.getView().setBackground(null);
//                        text.setBackground(shapeDrawable);
//                    }
//                }
//            }

                    if (textcolor != null) {
                        text.setTextColor(textcolor.intValue());
                    }

                    toast.setGravity(realGravity,0,0);
                    toast.show();
                } else {
                    int toastLength;
                    if (length.equals("long")) {
                        toastLength = Toast.LENGTH_LONG;
                    } else {
                        toastLength = Toast.LENGTH_SHORT;
                    }
                    Toast toast = Toast.makeText(ctx, msg, toastLength);
                    toast.setGravity(realGravity,0,0);
                    toast.show();
                }


                result.success(true);

                break;
            case "cancel":
                if (toast != null)
                    toast.cancel();
                result.success(true);
                break;
            default:
                result.notImplemented();
                break;
        }
    }
}
