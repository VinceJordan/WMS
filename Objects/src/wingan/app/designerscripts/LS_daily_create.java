package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_daily_create{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("actoolbarlight1").vw.setTop((int)((0d / 100 * height)));
views.get("actoolbarlight1").vw.setLeft((int)((0d / 100 * width)));
views.get("actoolbarlight1").vw.setWidth((int)((100d / 100 * width)));
views.get("actoolbarlight1").vw.setHeight((int)((8d / 100 * height)));
views.get("panel_template").vw.setTop((int)((8d / 100 * height)));
views.get("panel_template").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_template").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_template").vw.setHeight((int)((92d / 100 * height)));
views.get("label_template").vw.setTop((int)((1.5d / 100 * height)));
views.get("label_template").vw.setLeft((int)((3d / 100 * width)));
views.get("label_template").vw.setWidth((int)((80d / 100 * width)));
views.get("label_template").vw.setHeight((int)((5d / 100 * height)));
views.get("label_template_line").vw.setTop((int)((7.5d / 100 * height)));
views.get("label_template_line").vw.setLeft((int)((3d / 100 * width)));
views.get("label_template_line").vw.setWidth((int)((94d / 100 * width)));
views.get("label_template_line").vw.setHeight((int)((0.2d / 100 * height)));
views.get("button_edit").vw.setTop((int)((1.5d / 100 * height)));
views.get("button_edit").vw.setLeft((int)((80d / 100 * width)));
views.get("button_edit").vw.setWidth((int)((8d / 100 * width)));
views.get("button_edit").vw.setHeight((int)((5d / 100 * height)));
views.get("button_delete").vw.setTop((int)((1.5d / 100 * height)));
views.get("button_delete").vw.setLeft((int)((89d / 100 * width)));
views.get("button_delete").vw.setWidth((int)((8d / 100 * width)));
views.get("button_delete").vw.setHeight((int)((5d / 100 * height)));
views.get("label_group").vw.setTop((int)((9d / 100 * height)));
views.get("label_group").vw.setLeft((int)((3d / 100 * width)));
views.get("label_group").vw.setWidth((int)((27d / 100 * width)));
views.get("label_group").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_group").vw.setTop((int)((9d / 100 * height)));
views.get("label_load_group").vw.setLeft((int)((31d / 100 * width)));
views.get("label_load_group").vw.setWidth((int)((66d / 100 * width)));
views.get("label_load_group").vw.setHeight((int)((5d / 100 * height)));
views.get("label_year").vw.setTop((int)((15d / 100 * height)));
views.get("label_year").vw.setLeft((int)((3d / 100 * width)));
views.get("label_year").vw.setWidth((int)((14d / 100 * width)));
views.get("label_year").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_year").vw.setTop((int)((15d / 100 * height)));
views.get("label_load_year").vw.setLeft((int)((18d / 100 * width)));
views.get("label_load_year").vw.setWidth((int)((31.5d / 100 * width)));
views.get("label_load_year").vw.setHeight((int)((5d / 100 * height)));
views.get("label_month").vw.setTop((int)((15d / 100 * height)));
views.get("label_month").vw.setLeft((int)((50.5d / 100 * width)));
views.get("label_month").vw.setWidth((int)((14d / 100 * width)));
views.get("label_month").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_month").vw.setTop((int)((15d / 100 * height)));
views.get("label_load_month").vw.setLeft((int)((65.5d / 100 * width)));
views.get("label_load_month").vw.setWidth((int)((31.5d / 100 * width)));
views.get("label_load_month").vw.setHeight((int)((5d / 100 * height)));
views.get("product_template_table").vw.setTop((int)((22d / 100 * height)));
views.get("product_template_table").vw.setLeft((int)((0d / 100 * width)));
views.get("product_template_table").vw.setWidth((int)((100d / 100 * width)));
views.get("product_template_table").vw.setHeight((int)((70d / 100 * height)));
views.get("button_add").vw.setTop((int)((79d / 100 * height)));
views.get("button_add").vw.setLeft((int)((81d / 100 * width)));
views.get("button_add").vw.setWidth((int)((12d / 100 * width)));
views.get("button_add").vw.setHeight((int)((8d / 100 * height)));
views.get("button_copy").vw.setTop((int)((79d / 100 * height)));
views.get("button_copy").vw.setLeft((int)((67d / 100 * width)));
views.get("button_copy").vw.setWidth((int)((12d / 100 * width)));
views.get("button_copy").vw.setHeight((int)((8d / 100 * height)));
views.get("panel_bg_copy").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_copy").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_copy").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_copy").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_copy").vw.setTop((int)((30d / 100 * height)));
views.get("panel_copy").vw.setLeft((int)((4d / 100 * width)));
views.get("panel_copy").vw.setWidth((int)((92d / 100 * width)));
views.get("panel_copy").vw.setHeight((int)((29d / 100 * height)));
views.get("panel_copy_header").vw.setTop((int)((0d / 100 * height)));
views.get("panel_copy_header").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_copy_header").vw.setWidth((int)((92d / 100 * width)));
views.get("panel_copy_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_copy_header").vw.setTop((int)((0d / 100 * height)));
views.get("label_copy_header").vw.setLeft((int)((3d / 100 * width)));
views.get("label_copy_header").vw.setWidth((int)((77d / 100 * width)));
views.get("label_copy_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_copy_group").vw.setTop((int)((9d / 100 * height)));
views.get("label_copy_group").vw.setLeft((int)((3d / 100 * width)));
views.get("label_copy_group").vw.setWidth((int)((27d / 100 * width)));
views.get("label_copy_group").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_copy_group").vw.setTop((int)((9d / 100 * height)));
views.get("label_load_copy_group").vw.setLeft((int)((31d / 100 * width)));
views.get("label_load_copy_group").vw.setWidth((int)((58d / 100 * width)));
views.get("label_load_copy_group").vw.setHeight((int)((5d / 100 * height)));
views.get("label_copy_year").vw.setTop((int)((15d / 100 * height)));
views.get("label_copy_year").vw.setLeft((int)((3d / 100 * width)));
views.get("label_copy_year").vw.setWidth((int)((14d / 100 * width)));
views.get("label_copy_year").vw.setHeight((int)((5d / 100 * height)));
views.get("cmb_copy_year").vw.setTop((int)((15d / 100 * height)));
views.get("cmb_copy_year").vw.setLeft((int)((18d / 100 * width)));
views.get("cmb_copy_year").vw.setWidth((int)((27.5d / 100 * width)));
views.get("cmb_copy_year").vw.setHeight((int)((5d / 100 * height)));
views.get("label_copy_month").vw.setTop((int)((15d / 100 * height)));
views.get("label_copy_month").vw.setLeft((int)((46.5d / 100 * width)));
views.get("label_copy_month").vw.setWidth((int)((14d / 100 * width)));
views.get("label_copy_month").vw.setHeight((int)((5d / 100 * height)));
views.get("cmb_copy_month").vw.setTop((int)((15d / 100 * height)));
views.get("cmb_copy_month").vw.setLeft((int)((61.5d / 100 * width)));
views.get("cmb_copy_month").vw.setWidth((int)((27.5d / 100 * width)));
views.get("cmb_copy_month").vw.setHeight((int)((5d / 100 * height)));
views.get("button_save").vw.setTop((int)((22d / 100 * height)));
views.get("button_save").vw.setLeft((int)((68d / 100 * width)));
views.get("button_save").vw.setWidth((int)((20d / 100 * width)));
views.get("button_save").vw.setHeight((int)((6d / 100 * height)));
views.get("button_cancel").vw.setTop((int)((22d / 100 * height)));
views.get("button_cancel").vw.setLeft((int)((47d / 100 * width)));
views.get("button_cancel").vw.setWidth((int)((20d / 100 * width)));
views.get("button_cancel").vw.setHeight((int)((6d / 100 * height)));

}
}