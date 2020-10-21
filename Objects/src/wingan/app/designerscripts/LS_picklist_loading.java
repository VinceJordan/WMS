package wingan.app.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_picklist_loading{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("actoolbarlight1").vw.setTop((int)((0d / 100 * height)));
views.get("actoolbarlight1").vw.setLeft((int)((0d / 100 * width)));
views.get("actoolbarlight1").vw.setWidth((int)((100d / 100 * width)));
views.get("actoolbarlight1").vw.setHeight((int)((8d / 100 * height)));
views.get("loading_table").vw.setTop((int)((27d / 100 * height)));
views.get("loading_table").vw.setLeft((int)((0d / 100 * width)));
views.get("loading_table").vw.setWidth((int)((100d / 100 * width)));
views.get("loading_table").vw.setHeight((int)((73d / 100 * height)));
views.get("label_name").vw.setTop((int)((9d / 100 * height)));
views.get("label_name").vw.setLeft((int)((2d / 100 * width)));
views.get("label_name").vw.setWidth((int)((42d / 100 * width)));
views.get("label_name").vw.setHeight((int)((17d / 100 * height)));
views.get("label_load_name").vw.setTop((int)((13d / 100 * height)));
views.get("label_load_name").vw.setLeft((int)((3d / 100 * width)));
views.get("label_load_name").vw.setWidth((int)((40d / 100 * width)));
views.get("label_load_name").vw.setHeight((int)((8d / 100 * height)));
views.get("label_date").vw.setTop((int)((22d / 100 * height)));
views.get("label_date").vw.setLeft((int)((3d / 100 * width)));
views.get("label_date").vw.setWidth((int)((10d / 100 * width)));
views.get("label_date").vw.setHeight((int)((3d / 100 * height)));
views.get("label_load_date").vw.setTop((int)((22d / 100 * height)));
views.get("label_load_date").vw.setLeft((int)((13d / 100 * width)));
views.get("label_load_date").vw.setWidth((int)((30d / 100 * width)));
views.get("label_load_date").vw.setHeight((int)((3d / 100 * height)));
views.get("label_status").vw.setTop((int)((9d / 100 * height)));
views.get("label_status").vw.setLeft((int)((45d / 100 * width)));
views.get("label_status").vw.setWidth((int)((30d / 100 * width)));
views.get("label_status").vw.setHeight((int)((17d / 100 * height)));
views.get("label_load_status").vw.setTop((int)((13d / 100 * height)));
views.get("label_load_status").vw.setLeft((int)((46d / 100 * width)));
views.get("label_load_status").vw.setWidth((int)((28d / 100 * width)));
views.get("label_load_status").vw.setHeight((int)((6d / 100 * height)));
views.get("label_onhold").vw.setTop((int)((21d / 100 * height)));
views.get("label_onhold").vw.setLeft((int)((46d / 100 * width)));
views.get("label_onhold").vw.setWidth((int)((15d / 100 * width)));
views.get("label_onhold").vw.setHeight((int)((3d / 100 * height)));
views.get("label_load_onhold").vw.setTop((int)((20d / 100 * height)));
views.get("label_load_onhold").vw.setLeft((int)((62d / 100 * width)));
views.get("label_load_onhold").vw.setWidth((int)((12d / 100 * width)));
views.get("label_load_onhold").vw.setHeight((int)((5d / 100 * height)));
views.get("button_delivery").vw.setTop((int)((9d / 100 * height)));
views.get("button_delivery").vw.setLeft((int)((76d / 100 * width)));
views.get("button_delivery").vw.setWidth((int)((23d / 100 * width)));
views.get("button_delivery").vw.setHeight((int)((5d / 100 * height)));
views.get("button_autofill").vw.setTop((int)((15d / 100 * height)));
views.get("button_autofill").vw.setLeft((int)((76d / 100 * width)));
views.get("button_autofill").vw.setWidth((int)((23d / 100 * width)));
views.get("button_autofill").vw.setHeight((int)((5d / 100 * height)));
views.get("button_upload").vw.setTop((int)((21d / 100 * height)));
views.get("button_upload").vw.setLeft((int)((76d / 100 * width)));
views.get("button_upload").vw.setWidth((int)((23d / 100 * width)));
views.get("button_upload").vw.setHeight((int)((5d / 100 * height)));
views.get("panel_bg_type").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_type").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_type").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_type").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_type").vw.setTop((int)((30d / 100 * height)));
views.get("panel_type").vw.setLeft((int)((20d / 100 * width)));
views.get("panel_type").vw.setWidth((int)((60d / 100 * width)));
views.get("panel_type").vw.setHeight((int)((23d / 100 * height)));
views.get("panel_type_header").vw.setTop((int)((0d / 100 * height)));
views.get("panel_type_header").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_type_header").vw.setWidth((int)((60d / 100 * width)));
views.get("panel_type_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_type_header").vw.setTop((int)((0d / 100 * height)));
views.get("label_type_header").vw.setLeft((int)((3d / 100 * width)));
views.get("label_type_header").vw.setWidth((int)((57d / 100 * width)));
views.get("label_type_header").vw.setHeight((int)((7d / 100 * height)));
views.get("edittext_type").vw.setTop((int)((8d / 100 * height)));
views.get("edittext_type").vw.setLeft((int)((1d / 100 * width)));
views.get("edittext_type").vw.setWidth((int)((58d / 100 * width)));
views.get("edittext_type").vw.setHeight((int)((8d / 100 * height)));
views.get("button_load").vw.setTop((int)((17d / 100 * height)));
views.get("button_load").vw.setLeft((int)((43d / 100 * width)));
views.get("button_load").vw.setWidth((int)((16d / 100 * width)));
views.get("button_load").vw.setHeight((int)((5d / 100 * height)));
views.get("button_cancel").vw.setTop((int)((17d / 100 * height)));
views.get("button_cancel").vw.setLeft((int)((24d / 100 * width)));
views.get("button_cancel").vw.setWidth((int)((18d / 100 * width)));
views.get("button_cancel").vw.setHeight((int)((5d / 100 * height)));
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
views.get("panel_bg_security").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_security").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_security").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_security").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_security").vw.setTop((int)((30d / 100 * height)));
views.get("panel_security").vw.setLeft((int)((20d / 100 * width)));
views.get("panel_security").vw.setWidth((int)((60d / 100 * width)));
views.get("panel_security").vw.setHeight((int)((32d / 100 * height)));
views.get("panel_security_header").vw.setTop((int)((0d / 100 * height)));
views.get("panel_security_header").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_security_header").vw.setWidth((int)((60d / 100 * width)));
views.get("panel_security_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_security_header").vw.setTop((int)((0d / 100 * height)));
views.get("label_security_header").vw.setLeft((int)((3d / 100 * width)));
views.get("label_security_header").vw.setWidth((int)((57d / 100 * width)));
views.get("label_security_header").vw.setHeight((int)((7d / 100 * height)));
views.get("cmb_account").vw.setTop((int)((8d / 100 * height)));
views.get("cmb_account").vw.setLeft((int)((1d / 100 * width)));
views.get("cmb_account").vw.setWidth((int)((58d / 100 * width)));
views.get("cmb_account").vw.setHeight((int)((8d / 100 * height)));
views.get("edittext_password").vw.setTop((int)((17d / 100 * height)));
views.get("edittext_password").vw.setLeft((int)((1d / 100 * width)));
views.get("edittext_password").vw.setWidth((int)((58d / 100 * width)));
views.get("edittext_password").vw.setHeight((int)((8d / 100 * height)));
views.get("button_security_confirm").vw.setTop((int)((26d / 100 * height)));
views.get("button_security_confirm").vw.setLeft((int)((39d / 100 * width)));
views.get("button_security_confirm").vw.setWidth((int)((20d / 100 * width)));
views.get("button_security_confirm").vw.setHeight((int)((5d / 100 * height)));
views.get("button_security_cancel").vw.setTop((int)((26d / 100 * height)));
views.get("button_security_cancel").vw.setLeft((int)((20d / 100 * width)));
views.get("button_security_cancel").vw.setWidth((int)((18d / 100 * width)));
views.get("button_security_cancel").vw.setHeight((int)((5d / 100 * height)));
views.get("panel_bg_loading").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_loading").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_loading").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_loading").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_loading").vw.setTop((int)((7.5d / 100 * height)));
views.get("panel_loading").vw.setLeft((int)((5d / 100 * width)));
views.get("panel_loading").vw.setWidth((int)((90d / 100 * width)));
views.get("panel_loading").vw.setHeight((int)((85d / 100 * height)));
views.get("panel_title_loading").vw.setTop((int)((0d / 100 * height)));
views.get("panel_title_loading").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_title_loading").vw.setWidth((int)((90d / 100 * width)));
views.get("panel_title_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_title_loading").vw.setTop((int)((0d / 100 * height)));
views.get("label_title_loading").vw.setLeft((int)((3d / 100 * width)));
views.get("label_title_loading").vw.setWidth((int)((60d / 100 * width)));
views.get("label_title_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("button_exit_loading").vw.setTop((int)((0.5d / 100 * height)));
views.get("button_exit_loading").vw.setLeft((int)((80d / 100 * width)));
views.get("button_exit_loading").vw.setWidth((int)((8d / 100 * width)));
views.get("button_exit_loading").vw.setHeight((int)((6d / 100 * height)));
views.get("label_variant_loading").vw.setTop((int)((7d / 100 * height)));
views.get("label_variant_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_variant_loading").vw.setWidth((int)((50d / 100 * width)));
views.get("label_variant_loading").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_variant_loading").vw.setTop((int)((12d / 100 * height)));
views.get("label_load_variant_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_load_variant_loading").vw.setWidth((int)((86d / 100 * width)));
views.get("label_load_variant_loading").vw.setHeight((int)((5d / 100 * height)));
views.get("label_desc_loading").vw.setTop((int)((17d / 100 * height)));
views.get("label_desc_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_desc_loading").vw.setWidth((int)((30d / 100 * width)));
views.get("label_desc_loading").vw.setHeight((int)((5d / 100 * height)));
views.get("label_load_desc_loading").vw.setTop((int)((22d / 100 * height)));
views.get("label_load_desc_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_load_desc_loading").vw.setWidth((int)((86d / 100 * width)));
views.get("label_load_desc_loading").vw.setHeight((int)((5d / 100 * height)));
views.get("label_scancode").vw.setTop((int)((28d / 100 * height)));
views.get("label_scancode").vw.setLeft((int)((2d / 100 * width)));
views.get("label_scancode").vw.setWidth((int)((60d / 100 * width)));
views.get("label_scancode").vw.setHeight((int)((9d / 100 * height)));
views.get("label_load_scancode").vw.setTop((int)((28d / 100 * height)));
views.get("label_load_scancode").vw.setLeft((int)((63d / 100 * width)));
views.get("label_load_scancode").vw.setWidth((int)((25d / 100 * width)));
views.get("label_load_scancode").vw.setHeight((int)((9d / 100 * height)));
views.get("label_unit_loading").vw.setTop((int)((38d / 100 * height)));
views.get("label_unit_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_unit_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_unit_loading").vw.setHeight((int)((4d / 100 * height)));
views.get("label_pcs_unit_loading").vw.setTop((int)((42d / 100 * height)));
views.get("label_pcs_unit_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_pcs_unit_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_pcs_unit_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_case_unit_loading").vw.setTop((int)((49d / 100 * height)));
views.get("label_case_unit_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_case_unit_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_case_unit_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_box_unit_loading").vw.setTop((int)((56d / 100 * height)));
views.get("label_box_unit_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_box_unit_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_box_unit_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_doz_unit_loading").vw.setTop((int)((63d / 100 * height)));
views.get("label_doz_unit_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_doz_unit_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_doz_unit_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_pack_unit_loading").vw.setTop((int)((70d / 100 * height)));
views.get("label_pack_unit_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_pack_unit_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_pack_unit_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_bag_unit_loading").vw.setTop((int)((77d / 100 * height)));
views.get("label_bag_unit_loading").vw.setLeft((int)((2d / 100 * width)));
views.get("label_bag_unit_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_bag_unit_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_qty_prepared").vw.setTop((int)((38d / 100 * height)));
views.get("label_qty_prepared").vw.setLeft((int)((23.5d / 100 * width)));
views.get("label_qty_prepared").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_qty_prepared").vw.setHeight((int)((4d / 100 * height)));
views.get("label_pcs_qty_loading").vw.setTop((int)((42d / 100 * height)));
views.get("label_pcs_qty_loading").vw.setLeft((int)((23.5d / 100 * width)));
views.get("label_pcs_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_pcs_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_case_qty_loading").vw.setTop((int)((49d / 100 * height)));
views.get("label_case_qty_loading").vw.setLeft((int)((23.5d / 100 * width)));
views.get("label_case_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_case_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_box_qty_loading").vw.setTop((int)((56d / 100 * height)));
views.get("label_box_qty_loading").vw.setLeft((int)((23.5d / 100 * width)));
views.get("label_box_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_box_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_doz_qty_loading").vw.setTop((int)((63d / 100 * height)));
views.get("label_doz_qty_loading").vw.setLeft((int)((23.5d / 100 * width)));
views.get("label_doz_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_doz_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_pack_qty_loading").vw.setTop((int)((70d / 100 * height)));
views.get("label_pack_qty_loading").vw.setLeft((int)((23.5d / 100 * width)));
views.get("label_pack_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_pack_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_bag_qty_loading").vw.setTop((int)((77d / 100 * height)));
views.get("label_bag_qty_loading").vw.setLeft((int)((23.5d / 100 * width)));
views.get("label_bag_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_bag_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_qty_loading").vw.setTop((int)((38d / 100 * height)));
views.get("label_qty_loading").vw.setLeft((int)((45d / 100 * width)));
views.get("label_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_qty_loading").vw.setHeight((int)((4d / 100 * height)));
views.get("edittext_pcs_qty_loading").vw.setTop((int)((42d / 100 * height)));
views.get("edittext_pcs_qty_loading").vw.setLeft((int)((45d / 100 * width)));
views.get("edittext_pcs_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("edittext_pcs_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("edittext_case_qty_loading").vw.setTop((int)((49d / 100 * height)));
views.get("edittext_case_qty_loading").vw.setLeft((int)((45d / 100 * width)));
views.get("edittext_case_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("edittext_case_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("edittext_box_qty_loading").vw.setTop((int)((56d / 100 * height)));
views.get("edittext_box_qty_loading").vw.setLeft((int)((45d / 100 * width)));
views.get("edittext_box_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("edittext_box_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("edittext_doz_qty_loading").vw.setTop((int)((63d / 100 * height)));
views.get("edittext_doz_qty_loading").vw.setLeft((int)((45d / 100 * width)));
views.get("edittext_doz_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("edittext_doz_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("edittext_pack_qty_loading").vw.setTop((int)((70d / 100 * height)));
views.get("edittext_pack_qty_loading").vw.setLeft((int)((45d / 100 * width)));
views.get("edittext_pack_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("edittext_pack_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("edittext_bag_qty_loading").vw.setTop((int)((77d / 100 * height)));
views.get("edittext_bag_qty_loading").vw.setLeft((int)((45d / 100 * width)));
views.get("edittext_bag_qty_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("edittext_bag_qty_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("label_button").vw.setTop((int)((38d / 100 * height)));
views.get("label_button").vw.setLeft((int)((66.5d / 100 * width)));
views.get("label_button").vw.setWidth((int)((21.5d / 100 * width)));
views.get("label_button").vw.setHeight((int)((4d / 100 * height)));
views.get("button_pcs_loading").vw.setTop((int)((42d / 100 * height)));
views.get("button_pcs_loading").vw.setLeft((int)((66.5d / 100 * width)));
views.get("button_pcs_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("button_pcs_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("button_case_loading").vw.setTop((int)((49d / 100 * height)));
views.get("button_case_loading").vw.setLeft((int)((66.5d / 100 * width)));
views.get("button_case_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("button_case_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("button_box_loading").vw.setTop((int)((56d / 100 * height)));
views.get("button_box_loading").vw.setLeft((int)((66.5d / 100 * width)));
views.get("button_box_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("button_box_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("button_doz_loading").vw.setTop((int)((63d / 100 * height)));
views.get("button_doz_loading").vw.setLeft((int)((66.5d / 100 * width)));
views.get("button_doz_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("button_doz_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("button_pack_loading").vw.setTop((int)((70d / 100 * height)));
views.get("button_pack_loading").vw.setLeft((int)((66.5d / 100 * width)));
views.get("button_pack_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("button_pack_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("button_bag_loading").vw.setTop((int)((77d / 100 * height)));
views.get("button_bag_loading").vw.setLeft((int)((66.5d / 100 * width)));
views.get("button_bag_loading").vw.setWidth((int)((21.5d / 100 * width)));
views.get("button_bag_loading").vw.setHeight((int)((7d / 100 * height)));
views.get("panel_bg_trail").vw.setTop((int)((0d / 100 * height)));
views.get("panel_bg_trail").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_bg_trail").vw.setWidth((int)((100d / 100 * width)));
views.get("panel_bg_trail").vw.setHeight((int)((100d / 100 * height)));
views.get("panel_trail").vw.setTop((int)((3d / 100 * height)));
views.get("panel_trail").vw.setLeft((int)((20d / 100 * width)));
views.get("panel_trail").vw.setWidth((int)((60d / 100 * width)));
views.get("panel_trail").vw.setHeight((int)((94d / 100 * height)));
views.get("panel_trail_header").vw.setTop((int)((0d / 100 * height)));
views.get("panel_trail_header").vw.setLeft((int)((0d / 100 * width)));
views.get("panel_trail_header").vw.setWidth((int)((60d / 100 * width)));
views.get("panel_trail_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_trail_header").vw.setTop((int)((0d / 100 * height)));
views.get("label_trail_header").vw.setLeft((int)((3d / 100 * width)));
views.get("label_trail_header").vw.setWidth((int)((57d / 100 * width)));
views.get("label_trail_header").vw.setHeight((int)((7d / 100 * height)));
views.get("label_checker").vw.setTop((int)((8d / 100 * height)));
views.get("label_checker").vw.setLeft((int)((3d / 100 * width)));
views.get("label_checker").vw.setWidth((int)((27d / 100 * width)));
views.get("label_checker").vw.setHeight((int)((3d / 100 * height)));
views.get("label_load_checker").vw.setTop((int)((12d / 100 * height)));
views.get("label_load_checker").vw.setLeft((int)((3d / 100 * width)));
views.get("label_load_checker").vw.setWidth((int)((54d / 100 * width)));
views.get("label_load_checker").vw.setHeight((int)((8d / 100 * height)));
views.get("label_picker").vw.setTop((int)((21d / 100 * height)));
views.get("label_picker").vw.setLeft((int)((3d / 100 * width)));
views.get("label_picker").vw.setWidth((int)((12d / 100 * width)));
views.get("label_picker").vw.setHeight((int)((3d / 100 * height)));
views.get("cmb_picker").vw.setTop((int)((25d / 100 * height)));
views.get("cmb_picker").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 433;BA.debugLine="CMB_PICKER.Width = 54%x"[picklist_loading/General script]
views.get("cmb_picker").vw.setWidth((int)((54d / 100 * width)));
//BA.debugLineNum = 434;BA.debugLine="CMB_PICKER.Height = 8%y"[picklist_loading/General script]
views.get("cmb_picker").vw.setHeight((int)((8d / 100 * height)));
//BA.debugLineNum = 436;BA.debugLine="LABEL_SUPERVISOR.Top = 34%y"[picklist_loading/General script]
views.get("label_supervisor").vw.setTop((int)((34d / 100 * height)));
//BA.debugLineNum = 437;BA.debugLine="LABEL_SUPERVISOR.Left = 3%x"[picklist_loading/General script]
views.get("label_supervisor").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 438;BA.debugLine="LABEL_SUPERVISOR.Width = 18%x"[picklist_loading/General script]
views.get("label_supervisor").vw.setWidth((int)((18d / 100 * width)));
//BA.debugLineNum = 439;BA.debugLine="LABEL_SUPERVISOR.Height = 3%y"[picklist_loading/General script]
views.get("label_supervisor").vw.setHeight((int)((3d / 100 * height)));
//BA.debugLineNum = 441;BA.debugLine="CMB_SUPERVISOR.Top = 38%y"[picklist_loading/General script]
views.get("cmb_supervisor").vw.setTop((int)((38d / 100 * height)));
//BA.debugLineNum = 442;BA.debugLine="CMB_SUPERVISOR.Left = 3%x"[picklist_loading/General script]
views.get("cmb_supervisor").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 443;BA.debugLine="CMB_SUPERVISOR.Width = 54%x"[picklist_loading/General script]
views.get("cmb_supervisor").vw.setWidth((int)((54d / 100 * width)));
//BA.debugLineNum = 444;BA.debugLine="CMB_SUPERVISOR.Height = 8%y"[picklist_loading/General script]
views.get("cmb_supervisor").vw.setHeight((int)((8d / 100 * height)));
//BA.debugLineNum = 446;BA.debugLine="LABEL_PASSWORD.Top = 47%y"[picklist_loading/General script]
views.get("label_password").vw.setTop((int)((47d / 100 * height)));
//BA.debugLineNum = 447;BA.debugLine="LABEL_PASSWORD.Left = 3%x"[picklist_loading/General script]
views.get("label_password").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 448;BA.debugLine="LABEL_PASSWORD.Width = 18%x"[picklist_loading/General script]
views.get("label_password").vw.setWidth((int)((18d / 100 * width)));
//BA.debugLineNum = 449;BA.debugLine="LABEL_PASSWORD.Height = 3%y"[picklist_loading/General script]
views.get("label_password").vw.setHeight((int)((3d / 100 * height)));
//BA.debugLineNum = 451;BA.debugLine="EDITTEXT_TRAIL_PASS.Top = 51%y"[picklist_loading/General script]
views.get("edittext_trail_pass").vw.setTop((int)((51d / 100 * height)));
//BA.debugLineNum = 452;BA.debugLine="EDITTEXT_TRAIL_PASS.Left = 3%x"[picklist_loading/General script]
views.get("edittext_trail_pass").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 453;BA.debugLine="EDITTEXT_TRAIL_PASS.Width = 54%x"[picklist_loading/General script]
views.get("edittext_trail_pass").vw.setWidth((int)((54d / 100 * width)));
//BA.debugLineNum = 454;BA.debugLine="EDITTEXT_TRAIL_PASS.Height = 8%y"[picklist_loading/General script]
views.get("edittext_trail_pass").vw.setHeight((int)((8d / 100 * height)));
//BA.debugLineNum = 456;BA.debugLine="LABEL_REASON.Top = 60%y"[picklist_loading/General script]
views.get("label_reason").vw.setTop((int)((60d / 100 * height)));
//BA.debugLineNum = 457;BA.debugLine="LABEL_REASON.Left = 3%x"[picklist_loading/General script]
views.get("label_reason").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 458;BA.debugLine="LABEL_REASON.Width = 14%x"[picklist_loading/General script]
views.get("label_reason").vw.setWidth((int)((14d / 100 * width)));
//BA.debugLineNum = 459;BA.debugLine="LABEL_REASON.Height = 3%y"[picklist_loading/General script]
views.get("label_reason").vw.setHeight((int)((3d / 100 * height)));
//BA.debugLineNum = 461;BA.debugLine="CMB_REASON.Top = 64%y"[picklist_loading/General script]
views.get("cmb_reason").vw.setTop((int)((64d / 100 * height)));
//BA.debugLineNum = 462;BA.debugLine="CMB_REASON.Left = 3%x"[picklist_loading/General script]
views.get("cmb_reason").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 463;BA.debugLine="CMB_REASON.Width = 54%x"[picklist_loading/General script]
views.get("cmb_reason").vw.setWidth((int)((54d / 100 * width)));
//BA.debugLineNum = 464;BA.debugLine="CMB_REASON.Height = 8%y"[picklist_loading/General script]
views.get("cmb_reason").vw.setHeight((int)((8d / 100 * height)));
//BA.debugLineNum = 466;BA.debugLine="LABEL_UNIT.Top = 73%y"[picklist_loading/General script]
views.get("label_unit").vw.setTop((int)((73d / 100 * height)));
//BA.debugLineNum = 467;BA.debugLine="LABEL_UNIT.Left = 3%x"[picklist_loading/General script]
views.get("label_unit").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 468;BA.debugLine="LABEL_UNIT.Width = 12%x"[picklist_loading/General script]
views.get("label_unit").vw.setWidth((int)((12d / 100 * width)));
//BA.debugLineNum = 469;BA.debugLine="LABEL_UNIT.Height = 3%y"[picklist_loading/General script]
views.get("label_unit").vw.setHeight((int)((3d / 100 * height)));
//BA.debugLineNum = 471;BA.debugLine="CMB_UNIT.Top = 77%y"[picklist_loading/General script]
views.get("cmb_unit").vw.setTop((int)((77d / 100 * height)));
//BA.debugLineNum = 472;BA.debugLine="CMB_UNIT.Left = 3%x"[picklist_loading/General script]
views.get("cmb_unit").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 473;BA.debugLine="CMB_UNIT.Width = 26.5%x"[picklist_loading/General script]
views.get("cmb_unit").vw.setWidth((int)((26.5d / 100 * width)));
//BA.debugLineNum = 474;BA.debugLine="CMB_UNIT.Height = 8%y"[picklist_loading/General script]
views.get("cmb_unit").vw.setHeight((int)((8d / 100 * height)));
//BA.debugLineNum = 476;BA.debugLine="LABEL_QUANTITY.Top = 73%y"[picklist_loading/General script]
views.get("label_quantity").vw.setTop((int)((73d / 100 * height)));
//BA.debugLineNum = 477;BA.debugLine="LABEL_QUANTITY.Left = 30.5%x"[picklist_loading/General script]
views.get("label_quantity").vw.setLeft((int)((30.5d / 100 * width)));
//BA.debugLineNum = 478;BA.debugLine="LABEL_QUANTITY.Width = 15%x"[picklist_loading/General script]
views.get("label_quantity").vw.setWidth((int)((15d / 100 * width)));
//BA.debugLineNum = 479;BA.debugLine="LABEL_QUANTITY.Height = 3%y"[picklist_loading/General script]
views.get("label_quantity").vw.setHeight((int)((3d / 100 * height)));
//BA.debugLineNum = 481;BA.debugLine="EDITTEXT_QUANTITY.Top = 77%y"[picklist_loading/General script]
views.get("edittext_quantity").vw.setTop((int)((77d / 100 * height)));
//BA.debugLineNum = 482;BA.debugLine="EDITTEXT_QUANTITY.Left = 30.5%x"[picklist_loading/General script]
views.get("edittext_quantity").vw.setLeft((int)((30.5d / 100 * width)));
//BA.debugLineNum = 483;BA.debugLine="EDITTEXT_QUANTITY.Width = 26.5%x"[picklist_loading/General script]
views.get("edittext_quantity").vw.setWidth((int)((26.5d / 100 * width)));
//BA.debugLineNum = 484;BA.debugLine="EDITTEXT_QUANTITY.Height = 8%y"[picklist_loading/General script]
views.get("edittext_quantity").vw.setHeight((int)((8d / 100 * height)));
//BA.debugLineNum = 486;BA.debugLine="BUTTON_OKAY.Top = 87%y"[picklist_loading/General script]
views.get("button_okay").vw.setTop((int)((87d / 100 * height)));
//BA.debugLineNum = 487;BA.debugLine="BUTTON_OKAY.Left = 38%x"[picklist_loading/General script]
views.get("button_okay").vw.setLeft((int)((38d / 100 * width)));
//BA.debugLineNum = 488;BA.debugLine="BUTTON_OKAY.Width = 20%x"[picklist_loading/General script]
views.get("button_okay").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 489;BA.debugLine="BUTTON_OKAY.Height = 5%y"[picklist_loading/General script]
views.get("button_okay").vw.setHeight((int)((5d / 100 * height)));
//BA.debugLineNum = 492;BA.debugLine="PANEL_BG_DELIVERY.Top = 0%y"[picklist_loading/General script]
views.get("panel_bg_delivery").vw.setTop((int)((0d / 100 * height)));
//BA.debugLineNum = 493;BA.debugLine="PANEL_BG_DELIVERY.Left = 0%x"[picklist_loading/General script]
views.get("panel_bg_delivery").vw.setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 494;BA.debugLine="PANEL_BG_DELIVERY.Width = 100%x"[picklist_loading/General script]
views.get("panel_bg_delivery").vw.setWidth((int)((100d / 100 * width)));
//BA.debugLineNum = 495;BA.debugLine="PANEL_BG_DELIVERY.Height = 100%y"[picklist_loading/General script]
views.get("panel_bg_delivery").vw.setHeight((int)((100d / 100 * height)));
//BA.debugLineNum = 497;BA.debugLine="PANEL_DELIVERY.Top = 17%y"[picklist_loading/General script]
views.get("panel_delivery").vw.setTop((int)((17d / 100 * height)));
//BA.debugLineNum = 498;BA.debugLine="PANEL_DELIVERY.Left = 15%x"[picklist_loading/General script]
views.get("panel_delivery").vw.setLeft((int)((15d / 100 * width)));
//BA.debugLineNum = 499;BA.debugLine="PANEL_DELIVERY.Width = 70%x"[picklist_loading/General script]
views.get("panel_delivery").vw.setWidth((int)((70d / 100 * width)));
//BA.debugLineNum = 500;BA.debugLine="PANEL_DELIVERY.Height = 52%y"[picklist_loading/General script]
views.get("panel_delivery").vw.setHeight((int)((52d / 100 * height)));
//BA.debugLineNum = 502;BA.debugLine="PANEL_DELIVERY_HEADER.Top = 0%y"[picklist_loading/General script]
views.get("panel_delivery_header").vw.setTop((int)((0d / 100 * height)));
//BA.debugLineNum = 503;BA.debugLine="PANEL_DELIVERY_HEADER.Left = 0%x"[picklist_loading/General script]
views.get("panel_delivery_header").vw.setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 504;BA.debugLine="PANEL_DELIVERY_HEADER.Width = 70%x"[picklist_loading/General script]
views.get("panel_delivery_header").vw.setWidth((int)((70d / 100 * width)));
//BA.debugLineNum = 505;BA.debugLine="PANEL_DELIVERY_HEADER.Height = 7%y"[picklist_loading/General script]
views.get("panel_delivery_header").vw.setHeight((int)((7d / 100 * height)));
//BA.debugLineNum = 507;BA.debugLine="LABEL_DELIVERY_HEADER.Top = 0%y"[picklist_loading/General script]
views.get("label_delivery_header").vw.setTop((int)((0d / 100 * height)));
//BA.debugLineNum = 508;BA.debugLine="LABEL_DELIVERY_HEADER.Left = 3%x"[picklist_loading/General script]
views.get("label_delivery_header").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 509;BA.debugLine="LABEL_DELIVERY_HEADER.Width = 57%x"[picklist_loading/General script]
views.get("label_delivery_header").vw.setWidth((int)((57d / 100 * width)));
//BA.debugLineNum = 510;BA.debugLine="LABEL_DELIVERY_HEADER.Height = 7%y"[picklist_loading/General script]
views.get("label_delivery_header").vw.setHeight((int)((7d / 100 * height)));
//BA.debugLineNum = 512;BA.debugLine="LABEL_PLATE.Top = 9%y"[picklist_loading/General script]
views.get("label_plate").vw.setTop((int)((9d / 100 * height)));
//BA.debugLineNum = 513;BA.debugLine="LABEL_PLATE.Left = 3%x"[picklist_loading/General script]
views.get("label_plate").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 514;BA.debugLine="LABEL_PLATE.Width = 20%x"[picklist_loading/General script]
views.get("label_plate").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 515;BA.debugLine="LABEL_PLATE.Height = 6%y"[picklist_loading/General script]
views.get("label_plate").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 517;BA.debugLine="EDITTEXT_PLATE.Top = 9%y"[picklist_loading/General script]
views.get("edittext_plate").vw.setTop((int)((9d / 100 * height)));
//BA.debugLineNum = 518;BA.debugLine="EDITTEXT_PLATE.Left = 24%x"[picklist_loading/General script]
views.get("edittext_plate").vw.setLeft((int)((24d / 100 * width)));
//BA.debugLineNum = 519;BA.debugLine="EDITTEXT_PLATE.Width = 33%x"[picklist_loading/General script]
views.get("edittext_plate").vw.setWidth((int)((33d / 100 * width)));
//BA.debugLineNum = 520;BA.debugLine="EDITTEXT_PLATE.Height = 6%y"[picklist_loading/General script]
views.get("edittext_plate").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 522;BA.debugLine="BUTTON_SEARCH.Top = 9%y"[picklist_loading/General script]
views.get("button_search").vw.setTop((int)((9d / 100 * height)));
//BA.debugLineNum = 523;BA.debugLine="BUTTON_SEARCH.Left = 58%x"[picklist_loading/General script]
views.get("button_search").vw.setLeft((int)((58d / 100 * width)));
//BA.debugLineNum = 524;BA.debugLine="BUTTON_SEARCH.Width = 9%x"[picklist_loading/General script]
views.get("button_search").vw.setWidth((int)((9d / 100 * width)));
//BA.debugLineNum = 525;BA.debugLine="BUTTON_SEARCH.Height = 6%y"[picklist_loading/General script]
views.get("button_search").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 527;BA.debugLine="LABEL_DRIVER.Top = 16%y"[picklist_loading/General script]
views.get("label_driver").vw.setTop((int)((16d / 100 * height)));
//BA.debugLineNum = 528;BA.debugLine="LABEL_DRIVER.Left = 3%x"[picklist_loading/General script]
views.get("label_driver").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 529;BA.debugLine="LABEL_DRIVER.Width = 20%x"[picklist_loading/General script]
views.get("label_driver").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 530;BA.debugLine="LABEL_DRIVER.Height = 6%y"[picklist_loading/General script]
views.get("label_driver").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 532;BA.debugLine="EDITTEXT_DRIVER.Top = 16%y"[picklist_loading/General script]
views.get("edittext_driver").vw.setTop((int)((16d / 100 * height)));
//BA.debugLineNum = 533;BA.debugLine="EDITTEXT_DRIVER.Left = 24%x"[picklist_loading/General script]
views.get("edittext_driver").vw.setLeft((int)((24d / 100 * width)));
//BA.debugLineNum = 534;BA.debugLine="EDITTEXT_DRIVER.Width = 43%x"[picklist_loading/General script]
views.get("edittext_driver").vw.setWidth((int)((43d / 100 * width)));
//BA.debugLineNum = 535;BA.debugLine="EDITTEXT_DRIVER.Height = 6%y"[picklist_loading/General script]
views.get("edittext_driver").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 537;BA.debugLine="LABEL_HELPER1.Top = 23%y"[picklist_loading/General script]
views.get("label_helper1").vw.setTop((int)((23d / 100 * height)));
//BA.debugLineNum = 538;BA.debugLine="LABEL_HELPER1.Left = 3%x"[picklist_loading/General script]
views.get("label_helper1").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 539;BA.debugLine="LABEL_HELPER1.Width = 20%x"[picklist_loading/General script]
views.get("label_helper1").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 540;BA.debugLine="LABEL_HELPER1.Height = 6%y"[picklist_loading/General script]
views.get("label_helper1").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 542;BA.debugLine="EDITTEXT_HELPER1.Top = 23%y"[picklist_loading/General script]
views.get("edittext_helper1").vw.setTop((int)((23d / 100 * height)));
//BA.debugLineNum = 543;BA.debugLine="EDITTEXT_HELPER1.Left = 24%x"[picklist_loading/General script]
views.get("edittext_helper1").vw.setLeft((int)((24d / 100 * width)));
//BA.debugLineNum = 544;BA.debugLine="EDITTEXT_HELPER1.Width = 43%x"[picklist_loading/General script]
views.get("edittext_helper1").vw.setWidth((int)((43d / 100 * width)));
//BA.debugLineNum = 545;BA.debugLine="EDITTEXT_HELPER1.Height = 6%y"[picklist_loading/General script]
views.get("edittext_helper1").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 547;BA.debugLine="LABEL_HELPER2.Top = 30%y"[picklist_loading/General script]
views.get("label_helper2").vw.setTop((int)((30d / 100 * height)));
//BA.debugLineNum = 548;BA.debugLine="LABEL_HELPER2.Left = 3%x"[picklist_loading/General script]
views.get("label_helper2").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 549;BA.debugLine="LABEL_HELPER2.Width = 20%x"[picklist_loading/General script]
views.get("label_helper2").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 550;BA.debugLine="LABEL_HELPER2.Height = 6%y"[picklist_loading/General script]
views.get("label_helper2").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 552;BA.debugLine="EDITTEXT_HELPER2.Top = 30%y"[picklist_loading/General script]
views.get("edittext_helper2").vw.setTop((int)((30d / 100 * height)));
//BA.debugLineNum = 553;BA.debugLine="EDITTEXT_HELPER2.Left = 24%x"[picklist_loading/General script]
views.get("edittext_helper2").vw.setLeft((int)((24d / 100 * width)));
//BA.debugLineNum = 554;BA.debugLine="EDITTEXT_HELPER2.Width = 43%x"[picklist_loading/General script]
views.get("edittext_helper2").vw.setWidth((int)((43d / 100 * width)));
//BA.debugLineNum = 555;BA.debugLine="EDITTEXT_HELPER2.Height = 6%y"[picklist_loading/General script]
views.get("edittext_helper2").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 557;BA.debugLine="LABEL_HELPER3.Top = 37%y"[picklist_loading/General script]
views.get("label_helper3").vw.setTop((int)((37d / 100 * height)));
//BA.debugLineNum = 558;BA.debugLine="LABEL_HELPER3.Left = 3%x"[picklist_loading/General script]
views.get("label_helper3").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 559;BA.debugLine="LABEL_HELPER3.Width = 20%x"[picklist_loading/General script]
views.get("label_helper3").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 560;BA.debugLine="LABEL_HELPER3.Height = 6%y"[picklist_loading/General script]
views.get("label_helper3").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 562;BA.debugLine="EDITTEXT_HELPER3.Top = 37%y"[picklist_loading/General script]
views.get("edittext_helper3").vw.setTop((int)((37d / 100 * height)));
//BA.debugLineNum = 563;BA.debugLine="EDITTEXT_HELPER3.Left = 24%x"[picklist_loading/General script]
views.get("edittext_helper3").vw.setLeft((int)((24d / 100 * width)));
//BA.debugLineNum = 564;BA.debugLine="EDITTEXT_HELPER3.Width = 43%x"[picklist_loading/General script]
views.get("edittext_helper3").vw.setWidth((int)((43d / 100 * width)));
//BA.debugLineNum = 565;BA.debugLine="EDITTEXT_HELPER3.Height = 6%y"[picklist_loading/General script]
views.get("edittext_helper3").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 567;BA.debugLine="LABEL_PUSHCART.Top = 44%y"[picklist_loading/General script]
views.get("label_pushcart").vw.setTop((int)((44d / 100 * height)));
//BA.debugLineNum = 568;BA.debugLine="LABEL_PUSHCART.Left = 3%x"[picklist_loading/General script]
views.get("label_pushcart").vw.setLeft((int)((3d / 100 * width)));
//BA.debugLineNum = 569;BA.debugLine="LABEL_PUSHCART.Width = 20%x"[picklist_loading/General script]
views.get("label_pushcart").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 570;BA.debugLine="LABEL_PUSHCART.Height = 6%y"[picklist_loading/General script]
views.get("label_pushcart").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 572;BA.debugLine="EDITTEXT_PUSHCART.Top = 44%y"[picklist_loading/General script]
views.get("edittext_pushcart").vw.setTop((int)((44d / 100 * height)));
//BA.debugLineNum = 573;BA.debugLine="EDITTEXT_PUSHCART.Left = 24%x"[picklist_loading/General script]
views.get("edittext_pushcart").vw.setLeft((int)((24d / 100 * width)));
//BA.debugLineNum = 574;BA.debugLine="EDITTEXT_PUSHCART.Width = 15%x"[picklist_loading/General script]
views.get("edittext_pushcart").vw.setWidth((int)((15d / 100 * width)));
//BA.debugLineNum = 575;BA.debugLine="EDITTEXT_PUSHCART.Height = 6%y"[picklist_loading/General script]
views.get("edittext_pushcart").vw.setHeight((int)((6d / 100 * height)));
//BA.debugLineNum = 577;BA.debugLine="BUTTON_SAVE.Top = 44%y"[picklist_loading/General script]
views.get("button_save").vw.setTop((int)((44d / 100 * height)));
//BA.debugLineNum = 578;BA.debugLine="BUTTON_SAVE.Left = 47%x"[picklist_loading/General script]
views.get("button_save").vw.setLeft((int)((47d / 100 * width)));
//BA.debugLineNum = 579;BA.debugLine="BUTTON_SAVE.Width = 20%x"[picklist_loading/General script]
views.get("button_save").vw.setWidth((int)((20d / 100 * width)));
//BA.debugLineNum = 580;BA.debugLine="BUTTON_SAVE.Height = 6%y"[picklist_loading/General script]
views.get("button_save").vw.setHeight((int)((6d / 100 * height)));

}
}