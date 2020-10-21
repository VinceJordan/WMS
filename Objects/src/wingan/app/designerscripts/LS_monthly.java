package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_monthly{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("actoolbarlight1").vw.setTop((int)((0d / 100 * height)));
views.get("actoolbarlight1").vw.setLeft((int)((0d / 100 * width)));
views.get("actoolbarlight1").vw.setWidth((int)((100d / 100 * width)));
views.get("actoolbarlight1").vw.setHeight((int)((8d / 100 * height)));
views.get("panel_monthly").vw.setTop((int)((8d / 100 * height)));
views.get("panel_monthly").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_monthly").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_monthly").vw.setHeight((int)((92d / 100 * height)));
views.get("b4xtable1").vw.setTop((int)((0d / 100 * height)));
views.get("b4xtable1").vw.setLeft((int)((0d / 100 * width)));
views.get("b4xtable1").vw.setWidth((int)((100d / 100 * width)));
views.get("b4xtable1").vw.setHeight((int)((92d / 100 * height)));
views.get("panel_bg_new").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_new").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_new").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_new").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_new").vw.setTop((int)((30d / 100 * height)));
views.get("panel_new").vw.setLeft((int)((10d / 100 * width)));
views.get("panel_new").vw.setWidth((int)((80d / 100 * width)));
views.get("panel_new").vw.setHeight((int)((43d / 100 * height)));
views.get("panel_new_header").vw.setTop((int)((0d / 100 * height)));
views.get("panel_new_header").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_new_header").vw.setWidth((int)((80d / 100 * width)));
views.get("panel_new_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_new_header").vw.setTop((int)((0d / 100 * height)));
views.get("label_new_header").vw.setLeft((int)((3d / 100 * width)));
views.get("label_new_header").vw.setWidth((int)((77d / 100 * width)));
views.get("label_new_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_principal").vw.setTop((int)((10d / 100 * height)));
views.get("label_principal").vw.setLeft((int)((2d / 100 * width)));
views.get("label_principal").vw.setWidth((int)((17d / 100 * width)));
views.get("label_principal").vw.setHeight((int)((5d / 100 * height)));
views.get("cmb_principal").vw.setTop((int)((10d / 100 * height)));
views.get("cmb_principal").vw.setLeft((int)((18d / 100 * width)));
views.get("cmb_principal").vw.setWidth((int)((60d / 100 * width)));
views.get("cmb_principal").vw.setHeight((int)((5d / 100 * height)));
views.get("label_warehouse").vw.setTop((int)((16d / 100 * height)));
views.get("label_warehouse").vw.setLeft((int)((2d / 100 * width)));
views.get("label_warehouse").vw.setWidth((int)((20d / 100 * width)));
views.get("label_warehouse").vw.setHeight((int)((5d / 100 * height)));
views.get("cmb_warehouse").vw.setTop((int)((16d / 100 * height)));
views.get("cmb_warehouse").vw.setLeft((int)((22d / 100 * width)));
views.get("cmb_warehouse").vw.setWidth((int)((56d / 100 * width)));
views.get("cmb_warehouse").vw.setHeight((int)((5d / 100 * height)));
views.get("label_area").vw.setTop((int)((22d / 100 * height)));
views.get("label_area").vw.setLeft((int)((2d / 100 * width)));
views.get("label_area").vw.setWidth((int)((10d / 100 * width)));
views.get("label_area").vw.setHeight((int)((5d / 100 * height)));
views.get("cmb_area").vw.setTop((int)((22d / 100 * height)));
views.get("cmb_area").vw.setLeft((int)((12d / 100 * width)));
views.get("cmb_area").vw.setWidth((int)((66d / 100 * width)));
views.get("cmb_area").vw.setHeight((int)((5d / 100 * height)));
views.get("label_invdate").vw.setTop((int)((28d / 100 * height)));
views.get("label_invdate").vw.setLeft((int)((2d / 100 * width)));
views.get("label_invdate").vw.setWidth((int)((25d / 100 * width)));
views.get("label_invdate").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_invdate").vw.setTop((int)((28d / 100 * height)));
views.get("label_load_invdate").vw.setLeft((int)((27d / 100 * width)));
views.get("label_load_invdate").vw.setWidth((int)((51d / 100 * width)));
views.get("label_load_invdate").vw.setHeight((int)((5d / 100 * height)));
views.get("button_create").vw.setTop((int)((35d / 100 * height)));
views.get("button_create").vw.setLeft((int)((58d / 100 * width)));
views.get("button_create").vw.setWidth((int)((20d / 100 * width)));
views.get("button_create").vw.setHeight((int)((6d / 100 * height)));
views.get("button_cancel").vw.setTop((int)((35d / 100 * height)));
views.get("button_cancel").vw.setLeft((int)((37d / 100 * width)));
views.get("button_cancel").vw.setWidth((int)((20d / 100 * width)));
views.get("button_cancel").vw.setHeight((int)((6d / 100 * height)));

}
}