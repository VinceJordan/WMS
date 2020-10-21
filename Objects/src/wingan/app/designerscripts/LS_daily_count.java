package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_daily_count{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
if ((BA.ObjectToBoolean( String.valueOf(!anywheresoftware.b4a.keywords.LayoutBuilder.isPortrait())))) { 
;
views.get("actoolbarlight1").vw.setTop((int)((0d / 100 * height)));
views.get("actoolbarlight1").vw.setLeft((int)((0d / 100 * width)));
views.get("actoolbarlight1").vw.setWidth((int)((100d / 100 * width)));
views.get("actoolbarlight1").vw.setHeight((int)((13d / 100 * height)));
views.get("table_daily_inventory").vw.setTop((int)((26d / 100 * height)));
views.get("table_daily_inventory").vw.setLeft((int)((0d / 100 * width)));
views.get("table_daily_inventory").vw.setWidth((int)((100d / 100 * width)));
views.get("table_daily_inventory").vw.setHeight((int)((74d / 100 * height)));
views.get("button_prev").vw.setTop((int)((76d / 100 * height)));
views.get("button_prev").vw.setLeft((int)((5d / 100 * width)));
views.get("button_prev").vw.setWidth((int)((8d / 100 * width)));
views.get("button_prev").vw.setHeight((int)((13.5d / 100 * height)));
views.get("button_next").vw.setTop((int)((76d / 100 * height)));
views.get("button_next").vw.setLeft((int)((14d / 100 * width)));
views.get("button_next").vw.setWidth((int)((8d / 100 * width)));
views.get("button_next").vw.setHeight((int)((13.5d / 100 * height)));
views.get("panel_status").vw.setTop((int)((14d / 100 * height)));
views.get("panel_status").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_status").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_status").vw.setHeight((int)((23d / 100 * height)));
views.get("label_inv_date").vw.setTop((int)((2d / 100 * height)));
views.get("label_inv_date").vw.setLeft((int)((2d / 100 * width)));
views.get("label_inv_date").vw.setWidth((int)((16d / 100 * width)));
views.get("label_inv_date").vw.setHeight((int)((9d / 100 * height)));
views.get("label_load_inv_date").vw.setTop((int)((2d / 100 * height)));
views.get("label_load_inv_date").vw.setLeft((int)((19d / 100 * width)));
views.get("label_load_inv_date").vw.setWidth((int)((20d / 100 * width)));
views.get("label_load_inv_date").vw.setHeight((int)((9d / 100 * height)));
views.get("label_status").vw.setTop((int)((12d / 100 * height)));
views.get("label_status").vw.setLeft((int)((2d / 100 * width)));
views.get("label_status").vw.setWidth((int)((9d / 100 * width)));
views.get("label_status").vw.setHeight((int)((10d / 100 * height)));
views.get("label_load_status").vw.setTop((int)((12d / 100 * height)));
views.get("label_load_status").vw.setLeft((int)((12d / 100 * width)));
views.get("label_load_status").vw.setWidth((int)((27d / 100 * width)));
views.get("label_load_status").vw.setHeight((int)((10d / 100 * height)));
views.get("label_date").vw.setTop((int)((2d / 100 * height)));
views.get("label_date").vw.setLeft((int)((40d / 100 * width)));
views.get("label_date").vw.setWidth((int)((12d / 100 * width)));
views.get("label_date").vw.setHeight((int)((6d / 100 * height)));
views.get("label_load_date").vw.setTop((int)((2d / 100 * height)));
views.get("label_load_date").vw.setLeft((int)((53d / 100 * width)));
views.get("label_load_date").vw.setWidth((int)((25d / 100 * width)));
views.get("label_load_date").vw.setHeight((int)((6d / 100 * height)));
views.get("label_sysdate").vw.setTop((int)((9d / 100 * height)));
views.get("label_sysdate").vw.setLeft((int)((40d / 100 * width)));
views.get("label_sysdate").vw.setWidth((int)((12d / 100 * width)));
views.get("label_sysdate").vw.setHeight((int)((6d / 100 * height)));
views.get("label_load_sysdate").vw.setTop((int)((9d / 100 * height)));
views.get("label_load_sysdate").vw.setLeft((int)((53d / 100 * width)));
views.get("label_load_sysdate").vw.setWidth((int)((25d / 100 * width)));
views.get("label_load_sysdate").vw.setHeight((int)((6d / 100 * height)));
views.get("label_upldate").vw.setTop((int)((16d / 100 * height)));
views.get("label_upldate").vw.setLeft((int)((40d / 100 * width)));
views.get("label_upldate").vw.setWidth((int)((12d / 100 * width)));
views.get("label_upldate").vw.setHeight((int)((6d / 100 * height)));
views.get("label_load_upldate").vw.setTop((int)((16d / 100 * height)));
views.get("label_load_upldate").vw.setLeft((int)((53d / 100 * width)));
views.get("label_load_upldate").vw.setWidth((int)((25d / 100 * width)));
views.get("label_load_upldate").vw.setHeight((int)((6d / 100 * height)));
views.get("label_bluetooth_status").vw.setTop((int)((2d / 100 * height)));
views.get("label_bluetooth_status").vw.setLeft((int)((79d / 100 * width)));
views.get("label_bluetooth_status").vw.setWidth((int)((20d / 100 * width)));
views.get("label_bluetooth_status").vw.setHeight((int)((9d / 100 * height)));
views.get("label_page").vw.setTop((int)((13d / 100 * height)));
views.get("label_page").vw.setLeft((int)((79d / 100 * width)));
views.get("label_page").vw.setWidth((int)((20d / 100 * width)));
views.get("label_page").vw.setHeight((int)((9d / 100 * height)));
views.get("panel_bg_input").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_input").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_input").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_input").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_input").vw.setTop((int)((3d / 100 * height)));
views.get("panel_input").vw.setLeft((int)((4d / 100 * width)));
views.get("panel_input").vw.setWidth((int)((92d / 100 * width)));
views.get("panel_input").vw.setHeight((int)((90d / 100 * height)));
views.get("panel_input_header").vw.setTop((int)((0d / 100 * height)));
views.get("panel_input_header").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_input_header").vw.setWidth((int)((92d / 100 * width)));
views.get("panel_input_header").vw.setHeight((int)((12d / 100 * height)));
views.get("label_input_header").vw.setTop((int)((0d / 100 * height)));
views.get("label_input_header").vw.setLeft((int)((7.5d / 100 * width)));
views.get("label_input_header").vw.setWidth((int)((77d / 100 * width)));
views.get("label_input_header").vw.setHeight((int)((12d / 100 * height)));
views.get("button_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("button_calcu").vw.setLeft((int)((85d / 100 * width)));
views.get("button_calcu").vw.setWidth((int)((6d / 100 * width)));
views.get("button_calcu").vw.setHeight((int)((12d / 100 * height)));
views.get("button_exit").vw.setTop((int)((0d / 100 * height)));
views.get("button_exit").vw.setLeft((int)((.5d / 100 * width)));
views.get("button_exit").vw.setWidth((int)((6d / 100 * width)));
views.get("button_exit").vw.setHeight((int)((12d / 100 * height)));
views.get("label_principal").vw.setTop((int)((15d / 100 * height)));
views.get("label_principal").vw.setLeft((int)((2d / 100 * width)));
views.get("label_principal").vw.setWidth((int)((10d / 100 * width)));
views.get("label_principal").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_principal").vw.setTop((int)((21d / 100 * height)));
views.get("label_load_principal").vw.setLeft((int)((2d / 100 * width)));
views.get("label_load_principal").vw.setWidth((int)((50d / 100 * width)));
views.get("label_load_principal").vw.setHeight((int)((8d / 100 * height)));
views.get("label_variant").vw.setTop((int)((30d / 100 * height)));
views.get("label_variant").vw.setLeft((int)((2d / 100 * width)));
views.get("label_variant").vw.setWidth((int)((10d / 100 * width)));
views.get("label_variant").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_variant").vw.setTop((int)((36d / 100 * height)));
views.get("label_load_variant").vw.setLeft((int)((2d / 100 * width)));
views.get("label_load_variant").vw.setWidth((int)((50d / 100 * width)));
views.get("label_load_variant").vw.setHeight((int)((8d / 100 * height)));
views.get("label_description").vw.setTop((int)((45d / 100 * height)));
views.get("label_description").vw.setLeft((int)((2d / 100 * width)));
views.get("label_description").vw.setWidth((int)((10d / 100 * width)));
views.get("label_description").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_description").vw.setTop((int)((51d / 100 * height)));
views.get("label_load_description").vw.setLeft((int)((2d / 100 * width)));
views.get("label_load_description").vw.setWidth((int)((50d / 100 * width)));
views.get("label_load_description").vw.setHeight((int)((8d / 100 * height)));
views.get("label_reason").vw.setTop((int)((61d / 100 * height)));
views.get("label_reason").vw.setLeft((int)((2d / 100 * width)));
views.get("label_reason").vw.setWidth((int)((10d / 100 * width)));
views.get("label_reason").vw.setHeight((int)((5d / 100 * height)));
views.get("cmb_reason").vw.setTop((int)((67.5d / 100 * height)));
views.get("cmb_reason").vw.setLeft((int)((2d / 100 * width)));
views.get("cmb_reason").vw.setWidth((int)((50d / 100 * width)));
views.get("cmb_reason").vw.setHeight((int)((8d / 100 * height)));
views.get("label_unit").vw.setTop((int)((78d / 100 * height)));
views.get("label_unit").vw.setLeft((int)((2d / 100 * width)));
views.get("label_unit").vw.setWidth((int)((8d / 100 * width)));
views.get("label_unit").vw.setHeight((int)((8d / 100 * height)));
views.get("cmb_unit").vw.setTop((int)((78d / 100 * height)));
views.get("cmb_unit").vw.setLeft((int)((11d / 100 * width)));
views.get("cmb_unit").vw.setWidth((int)((15.5d / 100 * width)));
views.get("cmb_unit").vw.setHeight((int)((8d / 100 * height)));
views.get("label_quantity").vw.setTop((int)((78d / 100 * height)));
views.get("label_quantity").vw.setLeft((int)((27.5d / 100 * width)));
views.get("label_quantity").vw.setWidth((int)((11d / 100 * width)));
views.get("label_quantity").vw.setHeight((int)((8d / 100 * height)));
views.get("edittext_quantity").vw.setTop((int)((78d / 100 * height)));
views.get("edittext_quantity").vw.setLeft((int)((39.5d / 100 * width)));
views.get("edittext_quantity").vw.setWidth((int)((12.5d / 100 * width)));
views.get("edittext_quantity").vw.setHeight((int)((8d / 100 * height)));
views.get("label_list").vw.setTop((int)((15d / 100 * height)));
views.get("label_list").vw.setLeft((int)((54d / 100 * width)));
views.get("label_list").vw.setWidth((int)((10d / 100 * width)));
views.get("label_list").vw.setHeight((int)((5d / 100 * height)));
views.get("lvl_list").vw.setTop((int)((21d / 100 * height)));
views.get("lvl_list").vw.setLeft((int)((54d / 100 * width)));
views.get("lvl_list").vw.setWidth((int)((36d / 100 * width)));
views.get("lvl_list").vw.setHeight((int)((45d / 100 * height)));
views.get("label_actual").vw.setTop((int)((68d / 100 * height)));
views.get("label_actual").vw.setLeft((int)((54d / 100 * width)));
views.get("label_actual").vw.setWidth((int)((10d / 100 * width)));
views.get("label_actual").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_actual").vw.setTop((int)((68d / 100 * height)));
views.get("label_load_actual").vw.setLeft((int)((65d / 100 * width)));
views.get("label_load_actual").vw.setWidth((int)((14d / 100 * width)));
views.get("label_load_actual").vw.setHeight((int)((5d / 100 * height)));
views.get("label_add").vw.setTop((int)((74d / 100 * height)));
views.get("label_add").vw.setLeft((int)((54d / 100 * width)));
views.get("label_add").vw.setWidth((int)((10d / 100 * width)));
views.get("label_add").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_add").vw.setTop((int)((74d / 100 * height)));
views.get("label_load_add").vw.setLeft((int)((65d / 100 * width)));
views.get("label_load_add").vw.setWidth((int)((14d / 100 * width)));
views.get("label_load_add").vw.setHeight((int)((5d / 100 * height)));
views.get("label_deduct").vw.setTop((int)((80d / 100 * height)));
views.get("label_deduct").vw.setLeft((int)((54d / 100 * width)));
views.get("label_deduct").vw.setWidth((int)((10d / 100 * width)));
views.get("label_deduct").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_deduct").vw.setTop((int)((80d / 100 * height)));
views.get("label_load_deduct").vw.setLeft((int)((65d / 100 * width)));
views.get("label_load_deduct").vw.setWidth((int)((14d / 100 * width)));
views.get("label_load_deduct").vw.setHeight((int)((5d / 100 * height)));
views.get("button_cancel").vw.setTop((int)((68d / 100 * height)));
views.get("button_cancel").vw.setLeft((int)((80d / 100 * width)));
views.get("button_cancel").vw.setWidth((int)((10d / 100 * width)));
views.get("button_cancel").vw.setHeight((int)((8d / 100 * height)));
views.get("button_save").vw.setTop((int)((77d / 100 * height)));
views.get("button_save").vw.setLeft((int)((80d / 100 * width)));
views.get("button_save").vw.setWidth((int)((10d / 100 * width)));
views.get("button_save").vw.setHeight((int)((8d / 100 * height)));
views.get("panel_bg_msgbox").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_msgbox").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_msgbox").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_msgbox").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_msgbox").vw.setTop((int)((35d / 100 * height)));
views.get("panel_msgbox").vw.setLeft((int)((10d / 100 * width)));
views.get("panel_msgbox").vw.setWidth((int)((80d / 100 * width)));
views.get("panel_msgbox").vw.setHeight((int)((35d / 100 * height)));
views.get("panel_header_msgbox").vw.setTop((int)((0d / 100 * height)));
views.get("panel_header_msgbox").vw.setLeft((int)(0-(1d / 100 * width)));
views.get("panel_header_msgbox").vw.setWidth((int)((82d / 100 * width)));
views.get("panel_header_msgbox").vw.setHeight((int)((10d / 100 * height)));
views.get("label_header_logo").vw.setTop((int)((0d / 100 * height)));
views.get("label_header_logo").vw.setLeft((int)((1d / 100 * width)));
views.get("label_header_logo").vw.setWidth((int)((10d / 100 * width)));
views.get("label_header_logo").vw.setHeight((int)((10d / 100 * height)));
views.get("label_header_text").vw.setTop((int)((0d / 100 * height)));
views.get("label_header_text").vw.setLeft((int)((9d / 100 * width)));
views.get("label_header_text").vw.setWidth((int)((66d / 100 * width)));
views.get("label_header_text").vw.setHeight((int)((10d / 100 * height)));
views.get("label_msgbox1").vw.setTop((int)((12d / 100 * height)));
views.get("label_msgbox1").vw.setLeft((int)((3d / 100 * width)));
views.get("label_msgbox1").vw.setWidth((int)((74d / 100 * width)));
views.get("label_msgbox1").vw.setHeight((int)((7d / 100 * height)));
views.get("label_msgbox2").vw.setTop((int)((19d / 100 * height)));
views.get("label_msgbox2").vw.setLeft((int)((3d / 100 * width)));
views.get("label_msgbox2").vw.setWidth((int)((74d / 100 * width)));
views.get("label_msgbox2").vw.setHeight((int)((5d / 100 * height)));
views.get("pb_msgbox").vw.setTop((int)((27d / 100 * height)));
views.get("pb_msgbox").vw.setLeft((int)((2d / 100 * width)));
views.get("pb_msgbox").vw.setWidth((int)((76d / 100 * width)));
views.get("pb_msgbox").vw.setHeight((int)((4d / 100 * height)));
views.get("panel_bg_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_calcu").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_calcu").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_calcu").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_calcu").vw.setTop((int)((3d / 100 * height)));
views.get("panel_calcu").vw.setLeft((int)((25d / 100 * width)));
views.get("panel_calcu").vw.setWidth((int)((50d / 100 * width)));
views.get("panel_calcu").vw.setHeight((int)((90d / 100 * height)));
views.get("panel_header_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("panel_header_calcu").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_header_calcu").vw.setWidth((int)((50d / 100 * width)));
views.get("panel_header_calcu").vw.setHeight((int)((12d / 100 * height)));
views.get("label_header_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("label_header_calcu").vw.setLeft((int)((2d / 100 * width)));
views.get("label_header_calcu").vw.setWidth((int)((50d / 100 * width)));
views.get("label_header_calcu").vw.setHeight((int)((12d / 100 * height)));
views.get("button_exit_calcu").vw.setTop((int)((0d / 100 * height)));
views.get("button_exit_calcu").vw.setLeft((int)((43.5d / 100 * width)));
views.get("button_exit_calcu").vw.setWidth((int)((6d / 100 * width)));
views.get("button_exit_calcu").vw.setHeight((int)((12d / 100 * height)));
views.get("scvpaperroll").vw.setTop((int)((12d / 100 * height)));
views.get("scvpaperroll").vw.setLeft((int)((0d / 100 * width)));
views.get("scvpaperroll").vw.setWidth((int)((50d / 100 * width)));
views.get("scvpaperroll").vw.setHeight((int)((25d / 100 * height)));
views.get("panel_answer").vw.setTop((int)((37d / 100 * height)));
views.get("panel_answer").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_answer").vw.setWidth((int)((50d / 100 * width)));
views.get("panel_answer").vw.setHeight((int)((10d / 100 * height)));
views.get("label_answer").vw.setTop((int)((0d / 100 * height)));
views.get("label_answer").vw.setLeft((int)((3d / 100 * width)));
views.get("label_answer").vw.setWidth((int)((30d / 100 * width)));
views.get("label_answer").vw.setHeight((int)((10d / 100 * height)));
views.get("label_load_answer").vw.setTop((int)((0d / 100 * height)));
views.get("label_load_answer").vw.setLeft((int)((32d / 100 * width)));
views.get("label_load_answer").vw.setWidth((int)((15d / 100 * width)));
views.get("label_load_answer").vw.setHeight((int)((10d / 100 * height)));
views.get("pnlkeyboard").vw.setTop((int)((47d / 100 * height)));
views.get("pnlkeyboard").vw.setLeft((int)((0d / 100 * width)));
views.get("pnlkeyboard").vw.setWidth((int)((50d / 100 * width)));
views.get("pnlkeyboard").vw.setHeight((int)((43d / 100 * height)));
views.get("btn7").vw.setTop((int)((2d / 100 * height)));
views.get("btn7").vw.setLeft((int)((1d / 100 * width)));
views.get("btn7").vw.setWidth((int)((7d / 100 * width)));
views.get("btn7").vw.setHeight((int)((9d / 100 * height)));
views.get("btn4").vw.setTop((int)((12d / 100 * height)));
views.get("btn4").vw.setLeft((int)((1d / 100 * width)));
views.get("btn4").vw.setWidth((int)((7d / 100 * width)));
views.get("btn4").vw.setHeight((int)((9d / 100 * height)));
views.get("btn1").vw.setTop((int)((22d / 100 * height)));
views.get("btn1").vw.setLeft((int)((1d / 100 * width)));
views.get("btn1").vw.setWidth((int)((7d / 100 * width)));
views.get("btn1").vw.setHeight((int)((9d / 100 * height)));
views.get("btn0").vw.setTop((int)((32d / 100 * height)));
views.get("btn0").vw.setLeft((int)((1d / 100 * width)));
views.get("btn0").vw.setWidth((int)((7d / 100 * width)));
views.get("btn0").vw.setHeight((int)((9d / 100 * height)));
views.get("btn8").vw.setTop((int)((2d / 100 * height)));
views.get("btn8").vw.setLeft((int)((9d / 100 * width)));
views.get("btn8").vw.setWidth((int)((7d / 100 * width)));
views.get("btn8").vw.setHeight((int)((9d / 100 * height)));
views.get("btn5").vw.setTop((int)((12d / 100 * height)));
views.get("btn5").vw.setLeft((int)((9d / 100 * width)));
views.get("btn5").vw.setWidth((int)((7d / 100 * width)));
views.get("btn5").vw.setHeight((int)((9d / 100 * height)));
views.get("btn2").vw.setTop((int)((22d / 100 * height)));
views.get("btn2").vw.setLeft((int)((9d / 100 * width)));
views.get("btn2").vw.setWidth((int)((7d / 100 * width)));
views.get("btn2").vw.setHeight((int)((9d / 100 * height)));
views.get("btnp").vw.setTop((int)((32d / 100 * height)));
views.get("btnp").vw.setLeft((int)((9d / 100 * width)));
views.get("btnp").vw.setWidth((int)((7d / 100 * width)));
views.get("btnp").vw.setHeight((int)((9d / 100 * height)));
views.get("btn9").vw.setTop((int)((2d / 100 * height)));
views.get("btn9").vw.setLeft((int)((17d / 100 * width)));
views.get("btn9").vw.setWidth((int)((7d / 100 * width)));
views.get("btn9").vw.setHeight((int)((9d / 100 * height)));
views.get("btn6").vw.setTop((int)((12d / 100 * height)));
//BA.debugLineNum = 374;BA.debugLine="btn6.Left = 17%x"[daily_count/General script]
views.get("btn6").vw.setLeft((int)((17d / 100 * width)));
//BA.debugLineNum = 375;BA.debugLine="btn6.Width = 7%x"[daily_count/General script]
views.get("btn6").vw.setWidth((int)((7d / 100 * width)));
//BA.debugLineNum = 376;BA.debugLine="btn6.Height = 9%y"[daily_count/General script]
views.get("btn6").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 378;BA.debugLine="btn3.Top = 22%y"[daily_count/General script]
views.get("btn3").vw.setTop((int)((22d / 100 * height)));
//BA.debugLineNum = 379;BA.debugLine="btn3.Left = 17%x"[daily_count/General script]
views.get("btn3").vw.setLeft((int)((17d / 100 * width)));
//BA.debugLineNum = 380;BA.debugLine="btn3.Width = 7%x"[daily_count/General script]
views.get("btn3").vw.setWidth((int)((7d / 100 * width)));
//BA.debugLineNum = 381;BA.debugLine="btn3.Height = 9%y"[daily_count/General script]
views.get("btn3").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 383;BA.debugLine="btne.Top = 32%y"[daily_count/General script]
views.get("btne").vw.setTop((int)((32d / 100 * height)));
//BA.debugLineNum = 384;BA.debugLine="btne.Left = 17%x"[daily_count/General script]
views.get("btne").vw.setLeft((int)((17d / 100 * width)));
//BA.debugLineNum = 385;BA.debugLine="btne.Width = 7%x"[daily_count/General script]
views.get("btne").vw.setWidth((int)((7d / 100 * width)));
//BA.debugLineNum = 386;BA.debugLine="btne.Height = 9%y"[daily_count/General script]
views.get("btne").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 388;BA.debugLine="btna.Top = 2%y"[daily_count/General script]
views.get("btna").vw.setTop((int)((2d / 100 * height)));
//BA.debugLineNum = 389;BA.debugLine="btna.Left = 25%x"[daily_count/General script]
views.get("btna").vw.setLeft((int)((25d / 100 * width)));
//BA.debugLineNum = 390;BA.debugLine="btna.Width = 7%x"[daily_count/General script]
views.get("btna").vw.setWidth((int)((7d / 100 * width)));
//BA.debugLineNum = 391;BA.debugLine="btna.Height = 9%y"[daily_count/General script]
views.get("btna").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 393;BA.debugLine="btnb.Top = 12%y"[daily_count/General script]
views.get("btnb").vw.setTop((int)((12d / 100 * height)));
//BA.debugLineNum = 394;BA.debugLine="btnb.Left = 25%x"[daily_count/General script]
views.get("btnb").vw.setLeft((int)((25d / 100 * width)));
//BA.debugLineNum = 395;BA.debugLine="btnb.Width = 7%x"[daily_count/General script]
views.get("btnb").vw.setWidth((int)((7d / 100 * width)));
//BA.debugLineNum = 396;BA.debugLine="btnb.Height = 9%y"[daily_count/General script]
views.get("btnb").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 398;BA.debugLine="btnc.Top = 22%y"[daily_count/General script]
views.get("btnc").vw.setTop((int)((22d / 100 * height)));
//BA.debugLineNum = 399;BA.debugLine="btnc.Left = 25%x"[daily_count/General script]
views.get("btnc").vw.setLeft((int)((25d / 100 * width)));
//BA.debugLineNum = 400;BA.debugLine="btnc.Width = 7%x"[daily_count/General script]
views.get("btnc").vw.setWidth((int)((7d / 100 * width)));
//BA.debugLineNum = 401;BA.debugLine="btnc.Height = 9%y"[daily_count/General script]
views.get("btnc").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 403;BA.debugLine="btnd.Top = 32%y"[daily_count/General script]
views.get("btnd").vw.setTop((int)((32d / 100 * height)));
//BA.debugLineNum = 404;BA.debugLine="btnd.Left = 25%x"[daily_count/General script]
views.get("btnd").vw.setLeft((int)((25d / 100 * width)));
//BA.debugLineNum = 405;BA.debugLine="btnd.Width = 7%x"[daily_count/General script]
views.get("btnd").vw.setWidth((int)((7d / 100 * width)));
//BA.debugLineNum = 406;BA.debugLine="btnd.Height = 9%y"[daily_count/General script]
views.get("btnd").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 408;BA.debugLine="btnBack.Top = 2%y"[daily_count/General script]
views.get("btnback").vw.setTop((int)((2d / 100 * height)));
//BA.debugLineNum = 409;BA.debugLine="btnBack.Left = 33%x"[daily_count/General script]
views.get("btnback").vw.setLeft((int)((33d / 100 * width)));
//BA.debugLineNum = 410;BA.debugLine="btnBack.Width = 16%x"[daily_count/General script]
views.get("btnback").vw.setWidth((int)((16d / 100 * width)));
//BA.debugLineNum = 411;BA.debugLine="btnBack.Height = 9%y"[daily_count/General script]
views.get("btnback").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 413;BA.debugLine="btnClr.Top = 12%y"[daily_count/General script]
views.get("btnclr").vw.setTop((int)((12d / 100 * height)));
//BA.debugLineNum = 414;BA.debugLine="btnClr.Left = 33%x"[daily_count/General script]
views.get("btnclr").vw.setLeft((int)((33d / 100 * width)));
//BA.debugLineNum = 415;BA.debugLine="btnClr.Width = 16%x"[daily_count/General script]
views.get("btnclr").vw.setWidth((int)((16d / 100 * width)));
//BA.debugLineNum = 416;BA.debugLine="btnClr.Height = 9%y"[daily_count/General script]
views.get("btnclr").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 418;BA.debugLine="btnExit.Top = 32%y"[daily_count/General script]
views.get("btnexit").vw.setTop((int)((32d / 100 * height)));
//BA.debugLineNum = 419;BA.debugLine="btnExit.Left = 33%x"[daily_count/General script]
views.get("btnexit").vw.setLeft((int)((33d / 100 * width)));
//BA.debugLineNum = 420;BA.debugLine="btnExit.Width = 16%x"[daily_count/General script]
views.get("btnexit").vw.setWidth((int)((16d / 100 * width)));
//BA.debugLineNum = 421;BA.debugLine="btnExit.Height = 9%y"[daily_count/General script]
views.get("btnexit").vw.setHeight((int)((9d / 100 * height)));
//BA.debugLineNum = 423;BA.debugLine="End If"[daily_count/General script]
;};

}
}