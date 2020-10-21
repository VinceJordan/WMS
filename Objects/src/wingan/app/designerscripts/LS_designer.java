package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_designer{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("imgview_logo").vw.setTop((int)((29d / 100 * height)));
views.get("imgview_logo").vw.setLeft((int)((17.5d / 100 * width)));
views.get("imgview_logo").vw.setWidth((int)((65d / 100 * width)));
views.get("imgview_logo").vw.setHeight((int)((42d / 100 * height)));

}
}