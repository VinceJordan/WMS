package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_dashboard{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.1d);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("actoolbarlight1").vw.setTop((int)((0d / 100 * height)));
views.get("actoolbarlight1").vw.setLeft((int)((0d / 100 * width)));
views.get("actoolbarlight1").vw.setWidth((int)((100d / 100 * width)));
views.get("actoolbarlight1").vw.setHeight((int)((8d / 100 * height)));
views.get("panel_user").vw.setTop((int)((8d / 100 * height)));
views.get("panel_user").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_user").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_user").vw.setHeight((int)((10d / 100 * height)));
views.get("img_cover").vw.setTop((int)((0d / 100 * height)));
views.get("img_cover").vw.setLeft((int)((0d / 100 * width)));
views.get("img_cover").vw.setWidth((int)((100d / 100 * width)));
views.get("img_cover").vw.setHeight((int)((30d / 100 * height)));
views.get("img_profile_dash").vw.setTop((int)((1d / 100 * height)));
views.get("img_profile_dash").vw.setLeft((int)((4d / 100 * width)));
views.get("img_profile_dash").vw.setWidth((int)((13d / 100 * width)));
views.get("img_profile_dash").vw.setHeight((int)((8d / 100 * height)));
views.get("label_load_name_dash").vw.setTop((int)((1d / 100 * height)));
views.get("label_load_name_dash").vw.setLeft((int)((18d / 100 * width)));
views.get("label_load_name_dash").vw.setWidth((int)((55d / 100 * width)));
views.get("label_load_name_dash").vw.setHeight((int)((4d / 100 * height)));
views.get("label_load_dept_dash").vw.setTop((int)((4d / 100 * height)));
views.get("label_load_dept_dash").vw.setLeft((int)((18d / 100 * width)));
views.get("label_load_dept_dash").vw.setWidth((int)((68d / 100 * width)));
views.get("label_load_dept_dash").vw.setHeight((int)((3d / 100 * height)));
views.get("label_load_position_dash").vw.setTop((int)((6d / 100 * height)));
views.get("label_load_position_dash").vw.setLeft((int)((18d / 100 * width)));
views.get("label_load_position_dash").vw.setWidth((int)((68d / 100 * width)));
views.get("label_load_position_dash").vw.setHeight((int)((3d / 100 * height)));
views.get("label_load_position_dash").vw.setTop((int)((6d / 100 * height)));
views.get("label_load_position_dash").vw.setLeft((int)((18d / 100 * width)));
views.get("label_load_position_dash").vw.setWidth((int)((68d / 100 * width)));
views.get("label_load_position_dash").vw.setHeight((int)((3d / 100 * height)));
views.get("scrollview1").vw.setTop((int)((8d / 100 * height)));
views.get("scrollview1").vw.setLeft((int)((0d / 100 * width)));
views.get("scrollview1").vw.setWidth((int)((100d / 100 * width)));
views.get("scrollview1").vw.setHeight((int)((92d / 100 * height)));
views.get("panel_bg_msgbox").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_msgbox").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_msgbox").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_msgbox").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_msgbox").vw.setTop((int)((40d / 100 * height)));
views.get("panel_msgbox").vw.setLeft((int)((10d / 100 * width)));
views.get("panel_msgbox").vw.setWidth((int)((80d / 100 * width)));
views.get("panel_msgbox").vw.setHeight((int)((20d / 100 * height)));
views.get("panel_header_msgbox").vw.setTop((int)((0d / 100 * height)));
views.get("panel_header_msgbox").vw.setLeft((int)(0-(1d / 100 * width)));
views.get("panel_header_msgbox").vw.setWidth((int)((82d / 100 * width)));
views.get("panel_header_msgbox").vw.setHeight((int)((7d / 100 * height)));
views.get("label_header_logo").vw.setTop((int)((0d / 100 * height)));
views.get("label_header_logo").vw.setLeft((int)((2d / 100 * width)));
views.get("label_header_logo").vw.setWidth((int)((10d / 100 * width)));
views.get("label_header_logo").vw.setHeight((int)((7d / 100 * height)));
views.get("label_header_text").vw.setTop((int)((0d / 100 * height)));
views.get("label_header_text").vw.setLeft((int)((12d / 100 * width)));
views.get("label_header_text").vw.setWidth((int)((66d / 100 * width)));
views.get("label_header_text").vw.setHeight((int)((7d / 100 * height)));
views.get("label_msgbox1").vw.setTop((int)((8d / 100 * height)));
views.get("label_msgbox1").vw.setLeft((int)((3d / 100 * width)));
views.get("label_msgbox1").vw.setWidth((int)((74d / 100 * width)));
views.get("label_msgbox1").vw.setHeight((int)((4d / 100 * height)));
views.get("label_msgbox2").vw.setTop((int)((12d / 100 * height)));
views.get("label_msgbox2").vw.setLeft((int)((3d / 100 * width)));
views.get("label_msgbox2").vw.setWidth((int)((74d / 100 * width)));
views.get("label_msgbox2").vw.setHeight((int)((3d / 100 * height)));
views.get("pb_msgbox").vw.setTop((int)((17d / 100 * height)));
views.get("pb_msgbox").vw.setLeft((int)((2d / 100 * width)));
views.get("pb_msgbox").vw.setWidth((int)((76d / 100 * width)));
views.get("pb_msgbox").vw.setHeight((int)((2d / 100 * height)));

}
}