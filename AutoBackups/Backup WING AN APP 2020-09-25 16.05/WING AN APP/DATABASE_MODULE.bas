B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=9.801
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

#Extends: android.support.v7.app.AppCompatActivity

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	Dim connection As SQL
	Dim cursor1 As Cursor
	Dim cursor2 As Cursor
	Dim cursor3 As Cursor
	Dim cursor4 As Cursor
	Dim cursor5 As Cursor
	Dim cursor6 As Cursor
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Private ScrollView1 As ScrollView
	Private ACToolBarLight1 As ACToolBarLight
	Dim ToolbarHelper As ACActionBar
	Private LABEL_MSGBOX2 As Label
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_MSGBOX1 As Label
	Private LABEL_LAST_PRODUCTTBL As Label
	Private LABEL_LAST_WAREHOUSE As Label
	Private LABEL_LAST_PRINCIPAL As Label
	Private LABEL_LAST_EXPIRATION As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("database")
	
	ToolbarHelper.Initialize
	ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadBitmap(File.DirAssets, "LOGO_3D.png"))
	ToolbarHelper.ShowUpIndicator = True 'set to true to show the up arrow
	Dim bd As BitmapDrawable
	bd.Initialize(LoadBitmap(File.DirAssets, "back.png"))
	ToolbarHelper.UpIndicatorDrawable =  bd
	ACToolBarLight1.InitMenuListener
	
	Dim jo As JavaObject = ACToolBarLight1
	jo.RunMethod("setContentInsetStartWithNavigation", Array(5dip))
	jo.RunMethod("setTitleMarginStart", Array(10dip))
	
	ScrollView1.Panel.LoadLayout("database_scrollview")
	ScrollView1.Panel.Height = 92%y
	
	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", False)
	End If
	
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	LOG_PRINCIPAL
	Sleep(0)
	LOG_PRODUCTTBL
	Sleep(0)
	LOG_WAREHOUSE
	Sleep(0)
	LOG_EXPIRATION
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub SetAnimation(InAnimation As String, OutAnimation As String)
	Dim r As Reflector
	Dim package As String
	Dim in As Int
	Dim out As Int
	package = r.GetStaticField("anywheresoftware.b4a.BA", "packageName")
	in = r.GetStaticField(package & ".R$anim", InAnimation)
	out = r.GetStaticField(package & ".R$anim", OutAnimation)
	r.Target = r.GetActivity
	r.RunMethod4("overridePendingTransition", Array As Object(in, out), Array As String("java.lang.int", "java.lang.int"))
End Sub

Sub BitmapToBitmapDrawable (bitmap As Bitmap) As BitmapDrawable
	Dim bd As BitmapDrawable
	bd.Initialize(bitmap)
	Return bd
End Sub

Sub ACToolBarLight1_NavigationItemClick
	Activity.Finish
	StartActivity(DASHBOARD_MODULE)
	SetAnimation("left_to_center", "center_to_right")
End Sub

#Region SQL
Sub CreateRequest As DBRequestManager
	Dim req As DBRequestManager
	req.Initialize(Me, Main.rdclink)
	Return req
End Sub
Sub CreateCommand(Name As String, Parameters() As Object) As DBCommand
	Dim cmd As DBCommand
	cmd.Initialize
	cmd.Name = Name
	If Parameters <> Null Then cmd.Parameters = Parameters
	Return cmd
End Sub
Sub UPDATE_PRODUCTTBL
	ProgressDialogShow2("Loading...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_product_table", Null)
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		ProgressDialogHide
		Sleep(0)
		PANEL_BG_MSGBOX.BringToFront
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
		LABEL_MSGBOX2.Text = "Reading data..."
		connection.ExecNonQuery("DELETE FROM product_table")
		Sleep(0)
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		Log(2)
		For Each row() As Object In res.Rows
			connection.ExecNonQuery("INSERT INTO product_table VALUES ('"&row(res.Columns.Get("principal_id"))&"','"&row(res.Columns.Get("product_category"))& _
			"','"&row(res.Columns.Get("brand_img"))&"','"&row(res.Columns.Get("product_brand"))&"','"&row(res.Columns.Get("product_code"))& _
			"','"&row(res.Columns.Get("product_id"))&"','"&row(res.Columns.Get("product_variant"))&"','"&row(res.Columns.Get("product_desc_img"))&"','"&row(res.Columns.Get("product_desc"))& _
			"','"&row(res.Columns.Get("expiration_date"))&"','"&row(res.Columns.Get("case_bar_code"))&"','"&row(res.Columns.Get("bar_code"))&"','"&row(res.Columns.Get("box_bar_code"))& _
			"','"&row(res.Columns.Get("bag_bar_code"))&"','"&row(res.Columns.Get("pack_bar_code"))&"','"&row(res.Columns.Get("weight"))& _
			"','"&row(res.Columns.Get("unit_weight"))&"','"&row(res.Columns.Get("metric_tons"))&"','"&row(res.Columns.Get("flag_deleted"))&"','"&row(res.Columns.Get("prod_status"))& _
			"','"&row(res.Columns.Get("promo_product"))&"','"&row(res.Columns.Get("life_span_year"))&"','"&row(res.Columns.Get("life_span_month"))&"','"&row(res.Columns.Get("default_expiration_date_reading"))& _
			"','"&row(res.Columns.Get("inner_piece"))&"','"&row(res.Columns.Get("category_id"))&"','"&row(res.Columns.Get("datetime_modified"))&"','"&row(res.Columns.Get("total_audit"))& _
			"','"&row(res.Columns.Get("length_volume"))&"','"&row(res.Columns.Get("height_volume"))&"','"&row(res.Columns.Get("width_volume"))&"','"&row(res.Columns.Get("total_volume"))& _
			"','"&row(res.Columns.Get("effective_price_date"))&"','"&row(res.Columns.Get("PCS_UNIT_PER_PCS"))&"','"&row(res.Columns.Get("PCS_BOOKING"))&"','"&row(res.Columns.Get("PCS_EXTRUCK"))& _
			"','"&row(res.Columns.Get("PCS_COMPANY"))&"','"&row(res.Columns.Get("CASE_COMPANY"))&"','"&row(res.Columns.Get("CASE_EXTRUCK"))&"','"&row(res.Columns.Get("CASE_BOOKING"))& _
			"','"&row(res.Columns.Get("CASE_UNIT_PER_PCS"))&"','"&row(res.Columns.Get("DOZ_COMPANY"))&"','"&row(res.Columns.Get("DOZ_EXTRUCK"))&"','"&row(res.Columns.Get("DOZ_BOOKING"))& _
			"','"&row(res.Columns.Get("DOZ_UNIT_PER_PCS"))&"','"&row(res.Columns.Get("CASE_ACQUISITION"))&"','"&row(res.Columns.Get("PCS_ACQUISITION"))&"','"&row(res.Columns.Get("DOZ_ACQUISITION"))& _
			"','"&row(res.Columns.Get("CASE_SRP"))&"','"&row(res.Columns.Get("PCS_SRP"))&"','"&row(res.Columns.Get("DOZ_SRP"))&"','"&row(res.Columns.Get("BOX_ACQUISITION"))& _
			"','"&row(res.Columns.Get("BOX_SRP"))&"','"&row(res.Columns.Get("BOX_COMPANY"))&"','"&row(res.Columns.Get("BOX_EXTRUCK"))&"','"&row(res.Columns.Get("BOX_BOOKING"))& _
			"','"&row(res.Columns.Get("BOX_UNIT_PER_PCS"))&"','"&row(res.Columns.Get("BAG_ACQUISITION"))&"','"&row(res.Columns.Get("BAG_SRP"))&"','"&row(res.Columns.Get("BAG_COMPANY"))& _
			"','"&row(res.Columns.Get("BAG_EXTRUCK"))&"','"&row(res.Columns.Get("BAG_BOOKING"))&"','"&row(res.Columns.Get("BAG_UNIT_PER_PCS"))&"','"&row(res.Columns.Get("CASE_CP_GS"))& _
			"','"&row(res.Columns.Get("PCS_CP_GS"))&"','"&row(res.Columns.Get("DOZ_CP_GS"))&"','"&row(res.Columns.Get("BOX_CP_GS"))&"','"&row(res.Columns.Get("BAG_CP_GS"))& _
			"','"&row(res.Columns.Get("PACK_ACQUISITION"))&"','"&row(res.Columns.Get("PACK_SRP"))&"','"&row(res.Columns.Get("PACK_COMPANY"))&"','"&row(res.Columns.Get("PACK_EXTRUCK"))& _
			"','"&row(res.Columns.Get("PACK_BOOKING"))&"','"&row(res.Columns.Get("PACK_UNIT_PER_PCS"))&"','"&row(res.Columns.Get("PACK_CP_GS"))&"','"&row(res.Columns.Get("PACK_BAR"))& _
			"','"&row(res.Columns.Get("CASE_BAR"))&"','"&row(res.Columns.Get("PCS_BAR"))&"','"&row(res.Columns.Get("DOZ_BAR"))&"','"&row(res.Columns.Get("BOX_BAR"))& _
			"','"&row(res.Columns.Get("BAG_BAR"))&"','"&row(res.Columns.Get("PACK_FOTS"))&"','"&row(res.Columns.Get("CASE_FOTS"))&"','"&row(res.Columns.Get("PCS_FOTS"))& _
			"','"&row(res.Columns.Get("DOZ_FOTS"))&"','"&row(res.Columns.Get("BOX_FOTS"))&"','"&row(res.Columns.Get("BAG_FOTS"))&"','"&row(res.Columns.Get("PACK_PRINCE"))& _
			"','"&row(res.Columns.Get("CASE_PRINCE"))&"','"&row(res.Columns.Get("PCS_PRINCE"))&"','"&row(res.Columns.Get("DOZ_PRINCE"))&"','"&row(res.Columns.Get("BOX_PRINCE"))& _
			"','"&row(res.Columns.Get("BAG_PRINCE"))&"','"&row(res.Columns.Get("PACK_MARQUEE"))&"','"&row(res.Columns.Get("CASE_MARQUEE"))&"','"&row(res.Columns.Get("PCS_MARQUEE"))& _
			"','"&row(res.Columns.Get("DOZ_MARQUEE"))&"','"&row(res.Columns.Get("BOX_MARQUEE"))&"','"&row(res.Columns.Get("BAG_MARQUEE"))&"','"&row(res.Columns.Get("PACK_RCS_CHAIN"))& _
			"','"&row(res.Columns.Get("CASE_RCS_CHAIN"))&"','"&row(res.Columns.Get("PCS_RCS_CHAIN"))&"','"&row(res.Columns.Get("DOZ_RCS_CHAIN"))&"','"&row(res.Columns.Get("BOX_RCS_CHAIN"))& _
			"','"&row(res.Columns.Get("BAG_RCS_CHAIN"))&"')")
			Sleep(0)
			LABEL_MSGBOX2.Text = "Updating Product : " & row(res.Columns.Get("product_desc"))
		Next
		LABEL_MSGBOX2.Text = "Data Updated Successfully"
		Sleep(1000)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Sleep(0)
		connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Product Table'")
		Sleep(0)
		connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Product Table', '"&DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now)&"', '"&LOGIN_MODULE.username&"')")
		Sleep(0)
		LOG_PRODUCTTBL
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("PRODUCT TABLE IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub UPDATE_WAREHOUSE
	ProgressDialogShow2("Loading...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_warehouse", Null)
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		ProgressDialogHide
		PANEL_BG_MSGBOX.BringToFront
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
		LABEL_MSGBOX2.Text = "Reading data..."
		connection.ExecNonQuery("DELETE FROM warehouse_table")
		Sleep(0)
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
'		'work with result
		Log(2)
'		Dim get1 As String
		Dim gr As Int = 0
		For Each row() As Object In res.Rows
			gr = gr + 1
			connection.ExecNonQuery("INSERT INTO warehouse_table VALUES ('"&row(res.Columns.Get("warehouse_id"))&"','"&row(res.Columns.Get("warehouse_name"))& _
			"','"&row(res.Columns.Get("assigned_modules"))&"','"&row(res.Columns.Get("description"))&"','"&row(res.Columns.Get("date_time_registered"))& _
			"','"&row(res.Columns.Get("date_time_modified"))&"','"&row(res.Columns.Get("modified_flag"))&"','"&row(res.Columns.Get("edit_number"))&"','"&row(res.Columns.Get("user_info"))&"')")
			LABEL_MSGBOX2.Text = "Updating Warehouse : " & row(res.Columns.Get("warehouse_name"))
			Sleep(0)
		Next
		LABEL_MSGBOX2.Text = "Data Updated Successfully"
		Sleep(1000)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Warehouse Table'")
		Sleep(0)
		connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Warehouse Table', '"&DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now)&"', '"&LOGIN_MODULE.username&"')")
		Sleep(0)
		LOG_WAREHOUSE
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("WAREHOUSE DATABASE IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub UPDATE_PRINCIPAL
	ProgressDialogShow2("Loading...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_principal", Null)
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		ProgressDialogHide
		PANEL_BG_MSGBOX.BringToFront
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
		LABEL_MSGBOX2.Text = "Reading data..."
		connection.ExecNonQuery("DELETE FROM principal_table")
		Sleep(0)
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
'		'work with result
		Log(2)
		For Each row() As Object In res.Rows
			connection.ExecNonQuery("INSERT INTO principal_table VALUES ('"&row(res.Columns.Get("principal_id"))&"','"&row(res.Columns.Get("principal_code"))& _
			"','"&row(res.Columns.Get("principal_acronym"))&"','"&row(res.Columns.Get("mai_acronym"))&"','"&row(res.Columns.Get("principal_name"))& _
			"','"&row(res.Columns.Get("principal_address"))&"','"&row(res.Columns.Get("contact_person"))&"','"&row(res.Columns.Get("principal_tin"))&"','"&row(res.Columns.Get("principal_logo"))& _
			"','"&row(res.Columns.Get("trade_discount"))&"','"&row(res.Columns.Get("office_phone_no"))&"','"&row(res.Columns.Get("fax_no"))&"','"&row(res.Columns.Get("mobile_no"))& _
			"','"&row(res.Columns.Get("active_flag"))&"','"&row(res.Columns.Get("date_time_registered"))&"','"&row(res.Columns.Get("date_time_modified"))&"','"&row(res.Columns.Get("modified_flag"))& _
			"','"&row(res.Columns.Get("edit_number"))&"','"&row(res.Columns.Get("user_info"))&"')")
			Sleep(0)
			LABEL_MSGBOX2.Text = "Updating Principal : " & row(res.Columns.Get("principal_name"))

		Next
		LABEL_MSGBOX2.Text = "Data Updated Successfully"
		Sleep(1000)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Sleep(0)
		connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Principal Table'")
		Sleep(0)
		connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Principal Table', '"&DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now)&"', '"&LOGIN_MODULE.username&"')")
		Sleep(0)
		LOG_PRINCIPAL
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("PRINCIPAL DATABASE IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub DOWNLOAD_RECEIVING_EXPIRATION
	ProgressDialogShow2("Loading...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_expiration_receiving", Null)
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		ProgressDialogHide
		Sleep(0)
		PANEL_BG_MSGBOX.BringToFront
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
		LABEL_MSGBOX2.Text = "Reading data..."
		connection.ExecNonQuery("DELETE FROM product_expiration_ref_table")
		Sleep(0)
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		Log(2)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"&row(res.Columns.Get("dr_no"))&"','"&row(res.Columns.Get("principal_id"))& _
			"','"&row(res.Columns.Get("product_id"))&"','"&row(res.Columns.Get("product_variant"))&"','"&row(res.Columns.Get("product_description"))& _
			"','"&row(res.Columns.Get("unit"))&"','"&row(res.Columns.Get("quantity"))&"','"&row(res.Columns.Get("month_expired"))&"','"&row(res.Columns.Get("year_expired"))& _
			"','"&row(res.Columns.Get("date_expired"))&"','"&row(res.Columns.Get("date_registered"))&"','"&row(res.Columns.Get("time_registered"))&"','RECEIVING')")
				Sleep(0)
				LABEL_MSGBOX2.Text = "Downloading Expiration : " & row(res.Columns.Get("product_description"))
			Next
			DOWNLOAD_AUDIT_EXPIRATION
			Sleep(0)
			connection.ExecNonQuery("DELETE FROM database_update_table WHERE database ='Expiration Table'")
			Sleep(0)
			connection.ExecNonQuery("INSERT INTO database_update_table VALUES ('Expiration Table', '"&DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now)&"', '"&LOGIN_MODULE.username&"')")
			LABEL_MSGBOX2.Text = "Expiration Downloaded Successfully"
			Sleep(1000)
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			LOG_EXPIRATION
		Else
			ToastMessageShow("No expiration data for this Principal", False)
			Sleep(1000)
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		End If
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("EXPIRATION TABLE IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub DOWNLOAD_AUDIT_EXPIRATION
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_expiration_audit", Null)
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		Log(2)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"&row(res.Columns.Get("document_ref_no"))&"','"&row(res.Columns.Get("principal_id"))& _
			"','"&row(res.Columns.Get("product_id"))&"','"&row(res.Columns.Get("product_variant"))&"','"&row(res.Columns.Get("product_description"))& _
			"','"&row(res.Columns.Get("unit"))&"','"&row(res.Columns.Get("quantity"))&"','"&row(res.Columns.Get("month_expired"))&"','"&row(res.Columns.Get("year_expired"))& _
			"','"&row(res.Columns.Get("date_expired"))&"','"&row(res.Columns.Get("date_registered"))&"','"&row(res.Columns.Get("time_registered"))&"','AUDIT')")
				LABEL_MSGBOX2.Text = "Downloading Expiration : " & row(res.Columns.Get("product_description"))
			Next
			jr.Release
		End If
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("EXPIRATION TABLE IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
		jr.Release
	End If
End Sub
#End Region

Sub LOG_PRODUCTTBL
	cursor1 = connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Product Table'")
	If cursor1.RowCount > 0 Then
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			LABEL_LAST_PRODUCTTBL.Text = "Last Update: " & cursor1.GetString("date_time_updated") & " - " & cursor1.GetString("updated_by")
		Next
	Else
		
	End If
End Sub
Sub LOG_WAREHOUSE
	cursor2 = connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Warehouse Table'")
	If cursor2.RowCount > 0 Then
		For i = 0 To cursor2.RowCount - 1
			Sleep(0)
			cursor2.Position = i
			LABEL_LAST_WAREHOUSE.Text = "Last Update: " & cursor2.GetString("date_time_updated") & " - " & cursor2.GetString("updated_by")
		Next
	Else
		
	End If
End Sub
Sub LOG_PRINCIPAL
	cursor3 = connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Principal Table'")
	If cursor3.RowCount > 0 Then
		For i = 0 To cursor3.RowCount - 1
			Sleep(0)
			cursor3.Position = i
			LABEL_LAST_PRINCIPAL.Text = "Last Update: " & cursor3.GetString("date_time_updated") & " - " & cursor3.GetString("updated_by")
		Next
	Else
		
	End If
End Sub
Sub LOG_EXPIRATION
	cursor5 = connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Expiration Table'")
	If cursor5.RowCount > 0 Then
		For i = 0 To cursor5.RowCount - 1
			Sleep(0)
			cursor5.Position = i
		LABEL_LAST_EXPIRATION.Text = "Last Update: " & cursor5.GetString("date_time_updated") & " - " & cursor5.GetString("updated_by")
	Next
	Else
		
	End If
End Sub

Sub BUTTON_UPDATE_PRODUCTTBL_Click
	Msgbox2Async("Are you sure you want to update product data?" & CRLF & "Time span: 1-2 minutes", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		UPDATE_PRODUCTTBL
	Else
		
	End If
	
End Sub
Sub BUTTON_UPDATE_WAREHOUSE_Click
	Msgbox2Async("Are you sure you want to update warehouse data?" & CRLF & "Time span: 5 seconds", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		UPDATE_WAREHOUSE
	Else
		
	End If
End Sub
Sub BUTTON_UPDATE_PRINCIPAL_Click
	Msgbox2Async("Are you sure you want to update principal data?" & CRLF & "Time span: 5 seconds", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		UPDATE_PRINCIPAL
	Else
		
	End If
End Sub
Sub BUTTON_UPDATE_EXPIRATION_Click
	Msgbox2Async("Are you sure to update your local expiration data?", "Sync Expiration Data", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		DOWNLOAD_RECEIVING_EXPIRATION
	End If
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		Return True
	End If
End Sub