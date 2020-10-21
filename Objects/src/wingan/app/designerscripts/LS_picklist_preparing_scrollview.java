package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_picklist_preparing_scrollview{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("panel_preparing").vw.setTop((int)((0d / 100 * height)));
views.get("panel_preparing").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_preparing").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_preparing").vw.setHeight((int)((100d / 100 * height)));
views.get("preparing_table").vw.setTop((int)((0d / 100 * height)));
views.get("preparing_table").vw.setLeft((int)((0d / 100 * width)));
views.get("preparing_table").vw.setWidth((int)((100d / 100 * width)));
views.get("preparing_table").vw.setHeight((int)((100d / 100 * height)));

}
}