package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_monthly2{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("actoolbarlight1").vw.setTop((int)((0d / 100 * height)));
views.get("actoolbarlight1").vw.setLeft((int)((0d / 100 * width)));
views.get("actoolbarlight1").vw.setWidth((int)((100d / 100 * width)));
views.get("actoolbarlight1").vw.setHeight((int)((8d / 100 * height)));
views.get("sv_monthly").vw.setTop((int)((8d / 100 * height)));
views.get("sv_monthly").vw.setLeft((int)((0d / 100 * width)));
views.get("sv_monthly").vw.setWidth((int)((100d / 100 * width)));
views.get("sv_monthly").vw.setHeight((int)((92d / 100 * height)));
views.get("panel_bg_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_calcu").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_calcu").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_calcu").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_calcu").vw.setTop((int)((10d / 100 * height)));
views.get("panel_calcu").vw.setLeft((int)((15d / 100 * width)));
views.get("panel_calcu").vw.setWidth((int)((70d / 100 * width)));
views.get("panel_calcu").vw.setHeight((int)((79.5d / 100 * height)));
views.get("panel_header_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("panel_header_calcu").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_header_calcu").vw.setWidth((int)((70d / 100 * width)));
views.get("panel_header_calcu").vw.setHeight((int)((8d / 100 * height)));
views.get("label_header_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("label_header_calcu").vw.setLeft((int)((2d / 100 * width)));
views.get("label_header_calcu").vw.setWidth((int)((50d / 100 * width)));
views.get("label_header_calcu").vw.setHeight((int)((8d / 100 * height)));
views.get("button_exit_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("button_exit_calcu").vw.setLeft((int)((62.5d / 100 * width)));
views.get("button_exit_calcu").vw.setWidth((int)((6d / 100 * width)));
views.get("button_exit_calcu").vw.setHeight((int)((8d / 100 * height)));
views.get("scvpaperroll").vw.setTop((int)((10d / 100 * height)));
views.get("scvpaperroll").vw.setLeft((int)((2d / 100 * width)));
views.get("scvpaperroll").vw.setWidth((int)((66d / 100 * width)));
views.get("scvpaperroll").vw.setHeight((int)((25d / 100 * height)));
views.get("panel_answer").vw.setTop((int)((36d / 100 * height)));
views.get("panel_answer").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_answer").vw.setWidth((int)((70d / 100 * width)));
views.get("panel_answer").vw.setHeight((int)((8d / 100 * height)));
views.get("label_load_answer").vw.setTop((int)((0d / 100 * height)));
views.get("label_load_answer").vw.setLeft((int)((3d / 100 * width)));
views.get("label_load_answer").vw.setWidth((int)((62d / 100 * width)));
views.get("label_load_answer").vw.setHeight((int)((8d / 100 * height)));
views.get("pnlkeyboard").vw.setTop((int)((44d / 100 * height)));
views.get("pnlkeyboard").vw.setLeft((int)((0d / 100 * width)));
views.get("pnlkeyboard").vw.setWidth((int)((70d / 100 * width)));
views.get("pnlkeyboard").vw.setHeight((int)((35d / 100 * height)));
views.get("btn7").vw.setTop((int)((2d / 100 * height)));
views.get("btn7").vw.setLeft((int)((1d / 100 * width)));
views.get("btn7").vw.setWidth((int)((10d / 100 * width)));
views.get("btn7").vw.setHeight((int)((7d / 100 * height)));
views.get("btn4").vw.setTop((int)((10d / 100 * height)));
views.get("btn4").vw.setLeft((int)((1d / 100 * width)));
views.get("btn4").vw.setWidth((int)((10d / 100 * width)));
views.get("btn4").vw.setHeight((int)((7d / 100 * height)));
views.get("btn1").vw.setTop((int)((18d / 100 * height)));
views.get("btn1").vw.setLeft((int)((1d / 100 * width)));
views.get("btn1").vw.setWidth((int)((10d / 100 * width)));
views.get("btn1").vw.setHeight((int)((7d / 100 * height)));
views.get("btn0").vw.setTop((int)((26d / 100 * height)));
views.get("btn0").vw.setLeft((int)((1d / 100 * width)));
views.get("btn0").vw.setWidth((int)((10d / 100 * width)));
views.get("btn0").vw.setHeight((int)((7d / 100 * height)));
views.get("btn8").vw.setTop((int)((2d / 100 * height)));
views.get("btn8").vw.setLeft((int)((12d / 100 * width)));
views.get("btn8").vw.setWidth((int)((10d / 100 * width)));
views.get("btn8").vw.setHeight((int)((7d / 100 * height)));
views.get("btn5").vw.setTop((int)((10d / 100 * height)));
views.get("btn5").vw.setLeft((int)((12d / 100 * width)));
views.get("btn5").vw.setWidth((int)((10d / 100 * width)));
views.get("btn5").vw.setHeight((int)((7d / 100 * height)));
views.get("btn2").vw.setTop((int)((18d / 100 * height)));
views.get("btn2").vw.setLeft((int)((12d / 100 * width)));
views.get("btn2").vw.setWidth((int)((10d / 100 * width)));
views.get("btn2").vw.setHeight((int)((7d / 100 * height)));
views.get("btnp").vw.setTop((int)((26d / 100 * height)));
views.get("btnp").vw.setLeft((int)((12d / 100 * width)));
views.get("btnp").vw.setWidth((int)((10d / 100 * width)));
views.get("btnp").vw.setHeight((int)((7d / 100 * height)));
views.get("btn9").vw.setTop((int)((2d / 100 * height)));
views.get("btn9").vw.setLeft((int)((23d / 100 * width)));
views.get("btn9").vw.setWidth((int)((10d / 100 * width)));
views.get("btn9").vw.setHeight((int)((7d / 100 * height)));
views.get("btn6").vw.setTop((int)((10d / 100 * height)));
views.get("btn6").vw.setLeft((int)((23d / 100 * width)));
views.get("btn6").vw.setWidth((int)((10d / 100 * width)));
views.get("btn6").vw.setHeight((int)((7d / 100 * height)));
views.get("btn3").vw.setTop((int)((18d / 100 * height)));
views.get("btn3").vw.setLeft((int)((23d / 100 * width)));
views.get("btn3").vw.setWidth((int)((10d / 100 * width)));
views.get("btn3").vw.setHeight((int)((7d / 100 * height)));
views.get("btne").vw.setTop((int)((26d / 100 * height)));
views.get("btne").vw.setLeft((int)((23d / 100 * width)));
views.get("btne").vw.setWidth((int)((10d / 100 * width)));
views.get("btne").vw.setHeight((int)((7d / 100 * height)));
views.get("btna").vw.setTop((int)((2d / 100 * height)));
views.get("btna").vw.setLeft((int)((34d / 100 * width)));
views.get("btna").vw.setWidth((int)((10d / 100 * width)));
views.get("btna").vw.setHeight((int)((7d / 100 * height)));
views.get("btnb").vw.setTop((int)((10d / 100 * height)));
views.get("btnb").vw.setLeft((int)((34d / 100 * width)));
views.get("btnb").vw.setWidth((int)((10d / 100 * width)));
views.get("btnb").vw.setHeight((int)((7d / 100 * height)));
views.get("btnc").vw.setTop((int)((18d / 100 * height)));
views.get("btnc").vw.setLeft((int)((34d / 100 * width)));
views.get("btnc").vw.setWidth((int)((10d / 100 * width)));
views.get("btnc").vw.setHeight((int)((7d / 100 * height)));
views.get("btnd").vw.setTop((int)((26d / 100 * height)));
views.get("btnd").vw.setLeft((int)((34d / 100 * width)));
views.get("btnd").vw.setWidth((int)((10d / 100 * width)));
views.get("btnd").vw.setHeight((int)((7d / 100 * height)));
views.get("btnback").vw.setTop((int)((2d / 100 * height)));
views.get("btnback").vw.setLeft((int)((45d / 100 * width)));
views.get("btnback").vw.setWidth((int)((24d / 100 * width)));
views.get("btnback").vw.setHeight((int)((7d / 100 * height)));
views.get("btnclr").vw.setTop((int)((10d / 100 * height)));
views.get("btnclr").vw.setLeft((int)((45d / 100 * width)));
views.get("btnclr").vw.setWidth((int)((24d / 100 * width)));
views.get("btnclr").vw.setHeight((int)((7d / 100 * height)));
views.get("btnexit").vw.setTop((int)((26d / 100 * height)));
views.get("btnexit").vw.setLeft((int)((45d / 100 * width)));
views.get("btnexit").vw.setWidth((int)((24d / 100 * width)));
views.get("btnexit").vw.setHeight((int)((7d / 100 * height)));

}
}