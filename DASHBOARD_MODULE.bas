B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=9.801
@EndOfDesignText@
#Extends: android.support.v7.app.AppCompatActivity

#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

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
	
	Private logoutBitmap As Bitmap
	
	'tss
	Dim TTS1 As TTS

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Private ScrollView1 As ScrollView
	Private PANEL_RETURNS As Panel
	Private ACToolBarLight1 As ACToolBarLight
	Dim ToolbarHelper As ACActionBar
	Private xui As XUI
	
	Private Drawer As B4XDrawer
	Private LISTVIEW_MENU As ListView
	Private IMG_PROFILE As ImageView
	Private LABEL_LOAD_NAME As Label
	Private LABEL_LOAD_POSITION As Label
	Private LABEL_LOAD_DEPT As Label
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_MSGBOX2 As Label
	Private LABEL_MSGBOX1 As Label
	Private LABEL_HEADER_TEXT As Label
	Private BUTTON_RECEIVING As Button
	Private LABEL_LOAD_POSITION_DASH As Label
	Private LABEL_LOAD_DEPT_DASH As Label
	Private LABEL_LOAD_NAME_DASH As Label
	Private IMG_PROFILE_DASH As ImageView
	Private LABEL_LAST_PRODUCTTBL As Label
	Private LABEL_LAST_WAREHOUSE As Label
	Private LABEL_LAST_PRINCIPAL As Label
	Private LABEL_LAST_EXPIRATION As Label

End Sub

#If Java
	public boolean _onCreateOptionsMenu(android.view.Menu menu) {
		if (processBA.subExists("activity_createmenu")) {
			processBA.raiseEvent2(null, true, "activity_createmenu", false, new de.amberhome.objects.appcompat.ACMenuWrapper(menu));
			return true;
		}
		else
			return false;
	}
#End If

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	
	logoutBitmap = LoadBitmap(File.DirAssets, "logout.png")
	
	Drawer.Initialize(Me, "Drawer", Activity, 65%x)
	Drawer.CenterPanel.LoadLayout("dashboard")
	
	If File.Exists(File.DirRootExternal & "/WING AN APP/","tablet_db.db") = False Then
		File.Copy(File.DirAssets,"tablet_db.db", File.DirRootExternal & "/WING AN APP/","tablet_db.db")
	End If
	
	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", False)
	End If
	
	ToolbarHelper.Initialize
	ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadBitmap(File.DirAssets, "wms_logo.png"))
	ToolbarHelper.ShowUpIndicator = True 'set to true to show the up arrow
	Dim bd As BitmapDrawable
	bd.Initialize(LoadBitmap(File.DirAssets, "menu.png"))
	ToolbarHelper.UpIndicatorDrawable =  bd
	ACToolBarLight1.InitMenuListener
	Drawer.LeftPanel.LoadLayout("drawer")
	
	LOAD_LISTVIEW_MENU
	
	Dim jo As JavaObject = ACToolBarLight1
	jo.RunMethod("setContentInsetStartWithNavigation", Array(5dip))
	jo.RunMethod("setTitleMarginStart", Array(10dip))
	
	ScrollView1.Panel.LoadLayout("dashboard_scrollview")
	ScrollView1.Panel.Height = PANEL_RETURNS.Top + PANEL_RETURNS.Height + 8dip
	ScrollView1.Panel.Width = 120%x
	
	LOAD_USER_INFO
	Sleep(0)
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	If FirstTime = True Then
	VERIFY_PRODUCTTBL
	End If
	
	Log(LOGIN_MODULE.username)
End Sub

Sub Activity_Resume
	If TTS1.IsInitialized = False Then
		TTS1.Initialize("TTS1")
	End If
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

'This is the Sub called by the inline Java code to initialize the Menu
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "logout", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("logout", logoutBitmap)
End Sub

Sub LOAD_USER_INFO
	cursor1 = connection.ExecQuery("SELECT * FROM users_table where user = '"&LOGIN_MODULE.username&"'")
	If cursor1.RowCount > 0 Then
		For row = 0 To cursor1.RowCount - 1
			cursor1.Position = row
			Dim Buffer() As Byte 'declare an empty byte array
			Buffer = cursor1.GetBlob("Picture")
			Dim InputStream1 As InputStream
			InputStream1.InitializeFromBytesArray(Buffer, 0, Buffer.Length)
  
			Dim Bitmap1 As Bitmap
			Bitmap1.Initialize2(InputStream1)
			InputStream1.Close
			IMG_PROFILE.Bitmap = Bitmap1
			
			Dim xIV As B4XView = IMG_PROFILE
			xIV.SetBitmap(CreateRoundBitmap(Bitmap1, xIV .Width))
			
			LABEL_LOAD_NAME.Text = cursor1.GetString("User")
			LABEL_LOAD_DEPT.Text = "Deparment : " & cursor1.GetString("Department")
			LABEL_LOAD_POSITION.Text = "Position : " & cursor1.GetString("Position")
			
			Dim xIV As B4XView = IMG_PROFILE_DASH
			xIV.SetBitmap(CreateRoundBitmap(Bitmap1, xIV .Width))
			
			LABEL_LOAD_NAME_DASH.Text = cursor1.GetString("User")
			LABEL_LOAD_DEPT_DASH.Text = cursor1.GetString("Department")
			LABEL_LOAD_POSITION_DASH.Text = cursor1.GetString("Position")
'			user_change_edittext.Text = ser.GetString("Pass")
'			lim = ser.GetString("Pass")
'			user_change_edittext.Enabled = False
		Next
		GET_LOGIN
	End If
End Sub

'xui is a global XUI variable.
Sub CreateRoundBitmap (Input As B4XBitmap, Size As Int) As B4XBitmap
	If Input.Width <> Input.Height Then
		'if the image is not square then we crop it to be a square.
		Dim l As Int = Min(Input.Width, Input.Height)
		Input = Input.Crop(Input.Width / 2 - l / 2, Input.Height / 2 - l / 2, l, l)
	End If
	Dim c As B4XCanvas
	Dim xview As B4XView = xui.CreatePanel("")
	xview.SetLayoutAnimated(0, 0, 0, Size, Size)
	c.Initialize(xview)
	Dim path As B4XPath
	path.InitializeOval(c.TargetRect)
	c.ClipPath(path)
	c.DrawBitmap(Input.Resize(Size, Size, False), c.TargetRect)
	c.RemoveClip
'	c.DrawCircle(c.TargetRect.CenterX, c.TargetRect.CenterY, c.TargetRect.Width / 2 - 2dip, xui.Color_White, False, 5dip) 'comment this line to remove the border
	c.Invalidate
	Dim res As B4XBitmap = c.CreateBitmap
	c.Release
	Return res
End Sub

Sub LOAD_LISTVIEW_MENU
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.White, 5, 0, Colors.Black)
	LISTVIEW_MENU.Background = bg
	LISTVIEW_MENU.Clear
	LISTVIEW_MENU.SingleLineLayout.Label.Typeface = Typeface.FONTAWESOME
	LISTVIEW_MENU.SingleLineLayout.Label.TextSize = 16
	LISTVIEW_MENU.SingleLineLayout.label.Height = 6%y
	LISTVIEW_MENU.SingleLineLayout.ItemHeight = 6%y
	LISTVIEW_MENU.SingleLineLayout.Label.TextColor = Colors.Black
	LISTVIEW_MENU.SingleLineLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LISTVIEW_MENU.AddSingleLine("     Users Settings")
	LISTVIEW_MENU.AddSingleLine("     Update Database")
	LISTVIEW_MENU.AddSingleLine("     About Us")
	LISTVIEW_MENU.AddSingleLine("     Log-out")
End Sub

Sub BUTTON_MONTHLY_INVENTORY_Click
	Activity.Finish
	StartActivity(MONTHLY_INVENTORY_MODULE)
	SetAnimation("right_to_center", "center_to_left")
End Sub

Sub BUTTON_DAILY_INVENTORY_Click
	Activity.Finish
	StartActivity(DAILY_INVENTORY_MODULE)
	SetAnimation("right_to_center", "center_to_left")
	DAILY_INVENTORY_MODULE.clear_trigger = 1
End Sub

Sub BUTTON_PICKLIST_PREPARING_Click
	Activity.Finish
	StartActivity(PREPARING_MODULE)
	SetAnimation("right_to_center", "center_to_left")
End Sub

Sub BUTTON_PICKLIST_LOADING_Click
	Activity.Finish
	StartActivity(LOADING_MODULE)
	SetAnimation("right_to_center", "center_to_left")
End Sub

Sub BUTTON_EXPIRATION_Click
	Activity.Finish
	StartActivity(EXPIRATION_MODULE)
	SetAnimation("right_to_center", "center_to_left")
End Sub

Sub BUTTON_RECEIVING_Click
	Activity.Finish
	StartActivity(RECEIVING_MODULE)
	SetAnimation("right_to_center", "center_to_left")
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
Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK And Drawer.LeftOpen Then
		Drawer.LeftOpen = False
		Return True
	Else
		Return True
	End If
End Sub
Sub ACToolBarLight1_NavigationItemClick
	Drawer.LeftOpen = Not(Drawer.LeftOpen)
	LOG_PRINCIPAL
	Sleep(0)
	LOG_PRODUCTTBL
	Sleep(0)
	LOG_WAREHOUSE
	Sleep(0)
	LOG_EXPIRATION
End Sub
Sub UpdateIcon(MenuTitle As String, Icon As Bitmap)
	Dim m As ACMenuItem = GetMenuItem(MenuTitle)
	m.Icon = BitmapToBitmapDrawable(Icon)
End Sub
Sub GetMenuItem(Title As String) As ACMenuItem
	For i = 0 To ACToolBarLight1.Menu.Size - 1
		Dim m As ACMenuItem = ACToolBarLight1.Menu.GetItem(i)
		If m.Title = Title Then
			Return m
		End If
	Next
	Return Null
End Sub
Sub ACToolBarLight1_MenuItemClick (Item As ACMenuItem)
	If Item.Title = "logout" Then
		Msgbox2Async("Are you sure you want to log-out?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Activity.Finish
			StartActivity(LOGIN_MODULE)
			SetAnimation("left_to_center", "center_to_right")
			connection.ExecNonQuery("UPDATE user_token_table SET token = '0', last_logout = '"&DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now) &"'")
		End If
	End If
End Sub

Sub LISTVIEW_MENU_ItemClick (Position As Int, Value As Object)	
	If Position = 0 Then
		
	Else if Position = 1 Then
		Drawer.LeftOpen = False
		Sleep(0)
		StartActivity(DATABASE_MODULE)
		SetAnimation("right_to_center", "center_to_left")
		
	Else if Position = 2 Then
		
	Else if Position = 3 Then
		Msgbox2Async("Are you sure you want to log-out?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
		Activity.Finish
		StartActivity(LOGIN_MODULE)
		SetAnimation("left_to_center", "center_to_right")
			connection.ExecNonQuery("UPDATE user_token_table SET token = '0', last_logout = '"&DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now) &"'")
		End If
	End If
	
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
	Dim cmd As DBCommand = CreateCommand("select_expiration", Null)
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
				connection.ExecNonQuery("INSERT INTO product_expiration_ref_table VALUES ('"&row(res.Columns.Get("document_ref_no"))&"','"&row(res.Columns.Get("principal_id"))& _
			"','"&row(res.Columns.Get("product_id"))&"','"&row(res.Columns.Get("product_variant"))&"','"&row(res.Columns.Get("product_description"))& _
			"','"&row(res.Columns.Get("unit"))&"','"&row(res.Columns.Get("quantity"))&"','"&row(res.Columns.Get("month_expired"))&"','"&row(res.Columns.Get("year_expired"))& _
			"','"&row(res.Columns.Get("date_expired"))&"','"&row(res.Columns.Get("date_registered"))&"','"&row(res.Columns.Get("time_registered"))&"','"&row(res.Columns.Get("legend"))&"')")
				Sleep(0)
				LABEL_MSGBOX2.Text = "Downloading Expiration : " & row(res.Columns.Get("product_description"))
			Next
'			DOWNLOAD_AUDIT_EXPIRATION
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
#End Region

Sub GET_LOGIN	
		If LOGIN_MODULE.phone_id.GetDeviceId = "352948097840379" Then
			LOGIN_MODULE.tab_id = "DEBUGGER"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042456148" Then
			LOGIN_MODULE.tab_id = "A"		
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042438930" Then
			LOGIN_MODULE.tab_id = "B"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042452949" Then
			LOGIN_MODULE.tab_id = "C"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042466881" Then
			LOGIN_MODULE.tab_id = "D"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "359667094931063" Then
			LOGIN_MODULE.tab_id = "S-D"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042467103" Then
			LOGIN_MODULE.tab_id = "E"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "359667094930842" Then
			LOGIN_MODULE.tab_id = "S-E"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042658685" Then
			LOGIN_MODULE.tab_id = "F"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "359667094931014" Then
			LOGIN_MODULE.tab_id = "S-F"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042464068" Then
			LOGIN_MODULE.tab_id = "G"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042672041" Then
			LOGIN_MODULE.tab_id = "H"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "863129042450661" Then
			LOGIN_MODULE.tab_id = "I"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "356136101100354" Then
			LOGIN_MODULE.tab_id = "S-M"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "356136101101360" Then
			LOGIN_MODULE.tab_id = "S-N"
		else If LOGIN_MODULE.phone_id.GetDeviceId = "356136101096925" Then
			LOGIN_MODULE.tab_id = "S-O"
		Else
			Msgbox2Async("Your device is not registered, Please inform your IT Department" , "Block", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
End Sub

Sub VERIFY_PRODUCTTBL
	cursor5 = connection.ExecQuery("SELECT * FROM database_update_table WHERE database = 'Product Table' AND date_time_updated LIKE '%"&DateTime.Date(DateTime.Now)&"%'")
	If cursor5.RowCount > 0 Then
		For i = 0 To cursor5.RowCount - 1
			cursor5.Position = i
			Log(cursor5.GetString("date_time_updated"))
			Log(DateTime.Date(DateTime.Now))
		Next
	Else
		Log(DateTime.Date(DateTime.Now))
		Msgbox2Async("Hi, Good Day User! We would like to inform you that we will having an update of product table daily for the system data to remove any possible conflict. Press OK to proceed.", "Notice", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		UPDATE_PRODUCTTBL
	End If
End Sub

Sub TTS1_Ready (Success As Boolean)
	If Success Then
		'enable all views
'		Cell_Click 'play first sentence
	Else
'		Msgbox("Error initializing TTS engine.", "")
	End If
End Sub

Sub BUTTON_MONTHLY_SOUND_Click
	TTS1.Speak("Monthly Inventory, Inputting the inventory on-hand of actual stocks every monthly cutoff." , True)
End Sub

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
		Drawer.LeftOpen = False
		UPDATE_PRODUCTTBL
	Else
		
	End If
	
End Sub
Sub BUTTON_UPDATE_WAREHOUSE_Click
	Msgbox2Async("Are you sure you want to update warehouse data?" & CRLF & "Time span: 5 seconds", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Drawer.LeftOpen = False
		UPDATE_WAREHOUSE
	Else
		
	End If
End Sub
Sub BUTTON_UPDATE_PRINCIPAL_Click
	Msgbox2Async("Are you sure you want to update principal data?" & CRLF & "Time span: 5 seconds", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Drawer.LeftOpen = False
		UPDATE_PRINCIPAL
	Else
		
	End If
End Sub
Sub BUTTON_UPDATE_EXPIRATION_Click
	Msgbox2Async("Are you sure to update your local expiration data?", "Sync Expiration Data", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Drawer.LeftOpen = False
		DOWNLOAD_RECEIVING_EXPIRATION
	End If
End Sub

Sub BUTTON_PRICECHECK_Click
	Activity.Finish
	StartActivity(PRICE_CHECKING_MODULE)
	SetAnimation("right_to_center", "center_to_left")
End Sub

Sub BUTTON_RETURN_Click
	Activity.Finish
	StartActivity(RETURN_MODULE)
	SetAnimation("right_to_center", "center_to_left")
End Sub