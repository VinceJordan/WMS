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
	Dim cursor7 As Cursor
	Dim cursor8 As Cursor
	Dim cursor9 As Cursor
	Dim cursor10 As Cursor
	Dim cursor11 As Cursor
	Dim cursor12 As Cursor
	Dim cursor13 As Cursor
	Dim cursor14 As Cursor
	Dim cursor15 As Cursor
	Dim cursor16 As Cursor
	
	Dim clearBitmap As Bitmap
	Dim addBitmap As Bitmap
	Private cartBitmap As Bitmap
	
	Dim picklist_id As String
	
	Dim phone As Phone
	
	'tss
	Dim TTS1 As TTS
	
	'bluetooth
	Dim serial1 As Serial
	Dim AStream As AsyncStreams
	Dim Ts As Timer
	
	'variables for product
	Dim product_id As String
	Dim principal_id As String
	Dim principal_name As String
	Dim reason As String
	Dim scan_code As String
	Dim caseper As String
	Dim pcsper As String
	Dim dozper As String
	Dim boxper As String
	Dim bagper As String
	Dim packper As String
	Dim total_pieces As String
	Dim pcs_total_pieces As String
	Dim case_total_pieces As String
	Dim box_total_pieces As String
	Dim doz_total_pieces As String
	Dim pack_total_pieces As String
	Dim bag_total_pieces As String
	Dim total_order As Int
	Dim total_serve As Int
	Dim picker As String
	
	Dim security_trigger As String
	Dim scan_next_trigger As String
	Dim scan_go_trigger As String
	
	Dim preparing_count As String
	Dim downloaded_count As String
	Dim prepared_count As Int
	Dim uploaded_count As Int
	
	Dim load_go As String
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim CTRL As IME
	Dim BG_ENABLE As ColorDrawable
	Dim BG_DISABLE As ColorDrawable
	Dim BG_UPDATE As ColorDrawable
	
	'bluetooth
	Dim ScannerMacAddress As String
	Dim ScannerOnceConnected As Boolean
	
	Private XSelections As B4XTableSelections
	Private NameColumn(6) As B4XTableColumn
	
	Private cvs As B4XCanvas
	Private xui As XUI
	Private ACToolBarLight1 As ACToolBarLight
	Dim ToolbarHelper As ACActionBar
	Private PREPARING_TABLE As B4XTable
	Private PANEL_BG_TYPE As Panel
	Private EDITTEXT_TYPE As EditText
	Private LABEL_HEADER_TEXT As Label
	Private LABEL_MSGBOX1 As Label
	Private LABEL_MSGBOX2 As Label
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_LOAD_NAME As Label
	Private LABEL_LOAD_DATE As Label
	Private PANEL_PREPARING As Panel
	Private EDITTEXT_PCS_QTY_INPREPARED As EditText
	Private EDITTEXT_CASE_QTY_INPREPARED As EditText
	Private EDITTEXT_BOX_QTY_INPREPARED As EditText
	Private EDITTEXT_DOZ_QTY_INPREPARED As EditText
	Private EDITTEXT_PACK_QTY_INPREPARED As EditText
	Private EDITTEXT_BAG_QTY_INPREPARED As EditText
	Private LABEL_LOAD_VARIANT_PREPARING As Label
	Private LABEL_LOAD_DESC_PREPARING As Label
	Private LABEL_PCS_QTY_PREPARING As Label
	Private LABEL_CASE_QTY_PREPARING As Label
	Private LABEL_BOX_QTY_PREPARING As Label
	Private LABEL_DOZ_QTY_PREPARING As Label
	Private LABEL_PACK_QTY_PREPARING As Label
	Private LABEL_BAG_QTY_PREPARING As Label
	Private PANEL_BG_PREPARING As Panel
	Private BUTTON_PCS_PREPARING As Button
	Private BUTTON_CASE_PREPARING As Button
	Private BUTTON_BOX_PREPARING As Button
	Private BUTTON_PACK_PREPARING As Button
	Private BUTTON_DOZ_PREPARING As Button
	Private BUTTON_BAG_PREPARING As Button
	Private LABEL_LOAD_STATUS As Label
	Private LABEL_LOAD_ONHOLD As Label
	Private PANEL_BG_SECURITY As Panel
	Private CMB_ACCOUNT As B4XComboBox
	Private EDITTEXT_PASSWORD As EditText
	Private BUTTON_PREPARE As Button
	Private BUTTON_PICKER As Button
	Private BUTTON_AUTOFILL As Button
	Private CMB_PICKER As B4XComboBox
	Private PANEL_BG_TRAIL As Panel
	Private CMB_REASON As B4XComboBox
	Private CMB_UNIT As B4XComboBox
	Private EDITTEXT_QUANTITY As EditText
	Private BUTTON_OS As Button
	Private LABEL_LOAD_SCANCODE As Label
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
	Activity.LoadLayout("picklist_preparing")
	
	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", True)
	End If
	
	clearBitmap = LoadBitmap(File.DirAssets, "clear.png")
	addBitmap = LoadBitmap(File.DirAssets, "pencil.png")
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	
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
	
	'blueetooth
	serial1.Initialize("Serial")
	Ts.Initialize("Timer", 2000)
	
	Dim p As B4XView = xui.CreatePanel("")
	p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)
	cvs.Initialize(p)

	Dim bg As ColorDrawable
	bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_TYPE.Background = bg
	EDITTEXT_PASSWORD.Background = bg
	
	Dim bg2 As ColorDrawable
	bg2.Initialize2(Colors.RGB(182,217,255), 0, 1, Colors.White)
	EDITTEXT_PCS_QTY_INPREPARED.Background = bg2
	EDITTEXT_CASE_QTY_INPREPARED.Background = bg2
	EDITTEXT_DOZ_QTY_INPREPARED.Background = bg2
	EDITTEXT_BOX_QTY_INPREPARED.Background = bg2
	EDITTEXT_BAG_QTY_INPREPARED.Background = bg2
	EDITTEXT_PACK_QTY_INPREPARED.Background = bg2
	
	Dim bg3 As ColorDrawable
	bg3.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
		EDITTEXT_QUANTITY.Background = bg3
	
'	phone.SetScreenOrientation(0)
	
	Sleep(0)
	Dim Ref As Reflector
	Ref.Target = EDITTEXT_TYPE ' The text field being referenced
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
	picklist_id = ""
	
	Sleep(0)
	
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"

	BG_ENABLE.Initialize2(Colors.RGB(0,124,249),0,1,Colors.White)
	BG_DISABLE.Initialize2(Colors.RGB(174,174,174),0,1,Colors.White)
	BG_UPDATE.Initialize2(Colors.Yellow,0,1,Colors.White)

End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	CLEAR_PICKLIST
	Sleep(0)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "cart", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
End Sub

Sub Activity_Resume
	Log("Resuming...")
	ShowPairedDevices
	If ScannerOnceConnected=True Then
		Ts.Enabled=True
	End If
	If TTS1.IsInitialized = False Then
		TTS1.Initialize("TTS1")
	End If
End Sub
Sub Activity_Pause (UserClosed As Boolean)
	Log ("Activity paused. Disconnecting...")
	AStream.Close
	serial1.Disconnect
	Ts.Enabled=False
End Sub

Sub OpenSpinner(se As Spinner)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
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
	If Item.Title = "Load Picklist" Then
		PANEL_BG_TYPE.SetVisibleAnimated(300,True)
		PANEL_BG_TYPE.BringToFront
		EDITTEXT_TYPE.Text = ""
		EDITTEXT_TYPE.RequestFocus
		CTRL.ShowKeyboard(EDITTEXT_TYPE)
'	Else If Item.Title = "Download Template" Then
	Else If Item.Title = "cart" Then
		Log("Resuming...")
		cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
		UpdateIcon("cart", cartBitmap)
		ShowPairedDevices
		If ScannerOnceConnected=True Then
			Ts.Enabled=True
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connected.png")
			UpdateIcon("cart", cartBitmap)
		Else
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
			UpdateIcon("cart", cartBitmap)
		End If
	Else If Item.Title = "Clear" Then
		CLEAR_PICKLIST
	End If
End Sub

Sub BUTTON_CANCEL_Click
	PANEL_BG_TYPE.SetVisibleAnimated(300,False)
End Sub
Sub BUTTON_LOAD_Click
	picklist_id = EDITTEXT_TYPE.Text.ToUpperCase
	Sleep(0)
	VALIDATE_PICKLIST_STATUS
	Sleep(0)
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
Sub DOWNLOAD_PICKLIST
	LABEL_MSGBOX2.Text = "Getting Order..."
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_preparing_picklist", Array(picklist_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		connection.ExecNonQuery("DELETE FROM picklist_preparing_ref_table WHERE picklist_id = '"&picklist_id.Trim&"'")
		Sleep(0)
		connection.ExecNonQuery("UPDATE picklist_preparing_disc_table SET confirm_status = '1' WHERE picklist_id = '"&picklist_id.Trim&"'")
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				LABEL_LOAD_NAME.Text = row(res.Columns.Get("PickListName"))
				LABEL_LOAD_DATE.Text = row(res.Columns.Get("PickListDate"))
				LABEL_MSGBOX2.Text = "Getting Order : " & row(res.Columns.Get("prod_desc"))
				connection.ExecNonQuery("INSERT INTO picklist_preparing_ref_table VALUES ('"&row(res.Columns.Get("picklistID"))&"','"&row(res.Columns.Get("PickListName"))&"','"&row(res.Columns.Get("PickListDate"))& _
				"','"&row(res.Columns.Get("prod_id"))&"','"&row(res.Columns.Get("prod_brand"))&"','"&row(res.Columns.Get("prod_desc"))& _
				"','"&row(res.Columns.Get("unit"))&"','"&row(res.Columns.Get("qty"))&"','"&row(res.Columns.Get("total_pcs"))&"','"&DateTime.Date(DateTime.Now) &" " &DateTime.Time(DateTime.Now)&"','"&LOGIN_MODULE.tab_id&"','"&LOGIN_MODULE.username&"')")
				Sleep(0)
				connection.ExecNonQuery("UPDATE picklist_preparing_disc_table SET confirm_status = '0' WHERE picklist_id = '"&row(res.Columns.Get("picklistID"))&"' and product_id = '"&row(res.Columns.Get("prod_id"))&"' and unit = '"&row(res.Columns.Get("unit"))&"' and quantity = '"&row(res.Columns.Get("qty"))&"'")
				Sleep(0)
				connection.ExecNonQuery("UPDATE picklist_preparing_disc_table SET confirm_status = '0' WHERE picklist_id = '"&row(res.Columns.Get("picklistID"))&"' and product_id = '"&row(res.Columns.Get("prod_id"))&"' and quantity = '0'")
			Next
			CHECK_PICKLIST_PREPARING
			Sleep(0)
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			Msgbox2Async("The Picklist ID you type/scan is not existing in the system. Please double check your Picklist ID.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Sleep(0)
			CLEAR_PICKLIST
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub CHECK_PICKLIST_PREPARING
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_count_preparing_picklist", Array(picklist_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				preparing_count = row(res.Columns.Get("preparing_count"))
			Next
		Else
			preparing_count = 0
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
	Sleep(100)
	cursor8 = connection.ExecQuery("SELECT count(product_id) as 'downloaded_count' FROM picklist_preparing_ref_table WHERE picklist_id = '"&picklist_id.Trim&"'")
	If cursor8.RowCount > 0 Then
		For ise = 0 To cursor8.RowCount - 1
			cursor8.Position = ise
			downloaded_count = cursor8.GetString("downloaded_count")
'			downloaded_count = 15
		Next
	Else
		downloaded_count = 0
	End If
	Sleep(0)
	LABEL_MSGBOX2.Text = "Total Order : " & preparing_count & " " & "Total Downloaded : " & downloaded_count
	Sleep(2000)
	If downloaded_count <> preparing_count Then
		ToastMessageShow("Downloading Order Not Complete. Redownloading", False)
		Sleep(0)
		DOWNLOAD_PICKLIST
	Else
		Dim msg As String = "This are the sku that will be deleted or change quantity:" & CRLF
		cursor5 = connection.ExecQuery("SELECT * FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id.Trim&"' and confirm_status = '1'")
		If cursor5.RowCount > 0 Then
			For ise = 0 To cursor5.RowCount - 1
				cursor5.Position = ise
				msg = msg & " " & cursor5.GetString("product_description") & " / " & cursor5.GetString("quantity") & "-" & cursor5.GetString("unit") & CRLF
			Next
				
			Msgbox2Async(msg, "PICKLIST ADJUSTMENT", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
		connection.ExecNonQuery("DELETE FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id.Trim&"' and confirm_status = '1'")
		LABEL_MSGBOX2.Text = "Picklist Downloaded Successfully.."
		Sleep(500)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		PREPARING_TABLE.Clear
		LOAD_PREARING_HEADER
		Sleep(0)
		LOAD_PREPARING_PICKLIST
		CTRL.HideKeyboard
		Sleep(0)
		LOAD_PICKER
		Sleep(0)
		ENABLE_PICKLIST
		scan_next_trigger = 0
	End If
End Sub
Sub VALIDATE_PICKLIST_STATUS
	PANEL_BG_TYPE.SetVisibleAnimated(300,False)
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
	LABEL_HEADER_TEXT.Text = "Downloading Picklist"
	LABEL_MSGBOX2.Text = "Fetching data..."
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_picklist_status", Array(picklist_id.Trim))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				If row(res.Columns.Get("PickListStatus")) = "SAVED" Then
					UPDATE_PREPARING
				Else if row(res.Columns.Get("PickListStatus")) = "PREPARING" Then
					Log(98)
					If row(res.Columns.Get("PreparingTabletid")) = LOGIN_MODULE.tab_id And row(res.Columns.Get("PreparingChecker")) = LOGIN_MODULE.username  Then
						DOWNLOAD_PICKLIST
					Else
						Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")) & CRLF & _
						" is NOW PREPARING by :" & CRLF & _
						" Preparing Checker : " & row(res.Columns.Get("PreparingChecker")) & CRLF & _
						" Date & Time Preparing : " & row(res.Columns.Get("PreparingDateTIme")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("PreparingTabletid")) & CRLF & _
						" Do you want to overwrite this picklist?", _
			   			"Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						Wait For Msgbox_Result (Result As Int)
						If Result = DialogResponse.POSITIVE Then
							security_trigger = "OVERWRITE"
							GET_SECURITY
							Sleep(0)
							PANEL_BG_SECURITY.SetVisibleAnimated(300, True)
							PANEL_BG_SECURITY.BringToFront
							EDITTEXT_PASSWORD.RequestFocus
							Sleep(0)
							CTRL.ShowKeyboard(EDITTEXT_PASSWORD)
						Else
		
						End If
					End If
				Else if row(res.Columns.Get("PickListStatus")) = "PREPARED" Then
					If row(res.Columns.Get("PreparingTabletid")) = LOGIN_MODULE.tab_id And row(res.Columns.Get("PreparingChecker")) = LOGIN_MODULE.username Then
						Msgbox2Async("You've already prepared this picklist, Do you want to preparing check it again?", _
			   			"Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						Wait For Msgbox_Result (Result As Int)
						If Result = DialogResponse.POSITIVE Then
							DOWNLOAD_PICKLIST
						Else
		
						End If
					Else
						Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")) & CRLF & _
						" is already PREPARED by :" & CRLF & _
						" Preparing Checker : " & row(res.Columns.Get("PreparingChecker")) & CRLF & _
						" Date & Time Preparing : " & row(res.Columns.Get("PreparingDateTIme")) & CRLF & _
						" Date & Time Prepared : " & row(res.Columns.Get("PreparedDateTime")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("PreparingTabletid")), _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
					End If
				Else
					Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")) & CRLF & _
						" is already :" & row(res.Columns.Get("PickListStatus")) & CRLF & _
						" Preparing Checker : " & row(res.Columns.Get("PreparingChecker")) & CRLF & _
						" Date & Time Preparing : " & row(res.Columns.Get("PreparingDateTIme")) & CRLF & _
						" Date & Time Prepared : " & row(res.Columns.Get("PreparedDateTime")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("PreparingTabletid")), _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				End If
			Next
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			Msgbox2Async("The Picklist ID you type/scan is not existing in the system. Please double check your Picklist ID.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result
			CLEAR_PICKLIST
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub UPDATE_PREPARING
	LABEL_MSGBOX2.Text = "Updating Status..."
	Dim cmd As DBCommand = CreateCommand("update_picklist_preparing", Array(LOGIN_MODULE.tab_id,LOGIN_MODULE.username,DateTime.Date(DateTime.Now) &" "&DateTime.Time(DateTime.Now), picklist_id.Trim))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		DOWNLOAD_PICKLIST
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & js.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	js.Release
End Sub
#End Region

Sub LOAD_PREARING_HEADER
	NameColumn(0)=PREPARING_TABLE.AddColumn("Status", PREPARING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(1)=PREPARING_TABLE.AddColumn("Product Variant", PREPARING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(2)=PREPARING_TABLE.AddColumn("Product Description", PREPARING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(3)=PREPARING_TABLE.AddColumn("Unit", PREPARING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(4)=PREPARING_TABLE.AddColumn("Order", PREPARING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(5)=PREPARING_TABLE.AddColumn("Served", PREPARING_TABLE.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_PREPARING_PICKLIST
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor10 = connection.ExecQuery("SELECT a.*, b.prepared_status , b.prepared_quantity FROM picklist_preparing_ref_table as a LEFT OUTER JOIN picklist_preparing_disc_table as b ON " & _
	"a.picklist_id = b.picklist_id And a.product_id = b.product_id And a.unit = b.unit " & _
	"WHERE a.picklist_id = '"&picklist_id&"' " & _
	"ORDER BY b.prepared_status, a.product_variant ASC")
	If cursor10.RowCount > 0 Then
		For ia = 0 To cursor10.RowCount - 1
			Sleep(10)
			cursor10.Position = ia
			If cursor10.GetString("prepared_status") = Null Or cursor10.GetString("prepared_status") = "" Then
				Sleep(10)
				Dim row(6) As Object
				row(0) = "NOT PREPARED"
				row(1) = cursor10.GetString("product_variant")
				row(2) = cursor10.GetString("product_description")
				row(3) = cursor10.GetString("unit")
				row(4) = cursor10.GetString("quantity")
				row(5) = 0
				Data.Add(row)		
			Else
				Dim row(6) As Object
				row(0) = cursor10.GetString("prepared_status")
				row(1) = cursor10.GetString("product_variant")
				row(2) = cursor10.GetString("product_description")
				row(3) = cursor10.GetString("unit")
				row(4) = cursor10.GetString("quantity")
				row(5) = cursor10.GetString("prepared_quantity")
				Data.Add(row)
			End If
		Next	
			cursor11 = connection.ExecQuery("SELECT * FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id&"' and quantity = '0'")
			If cursor11.RowCount > 0 Then
				For ic = 0 To cursor11.RowCount - 1
					cursor11.Position = ic
					Sleep(10)
					Dim row(6) As Object
					row(0) = cursor11.GetString("prepared_status")
					row(1) = cursor11.GetString("product_variant")
					row(2) = cursor11.GetString("product_description")
					row(3) = cursor11.GetString("unit")
					row(4) = cursor11.GetString("quantity")
					row(5) = cursor11.GetString("prepared_quantity")
					Data.Add(row)
				Next
				
				
			Else
				
				
			End If
		PREPARING_TABLE.NumberOfFrozenColumns = 1
		PREPARING_TABLE.RowHeight = 50dip
		Sleep(100)
		GET_STATUS
		Sleep(0)
		ProgressDialogHide
	Else
		ToastMessageShow("Picklist is empty", False)
	End If
	PREPARING_TABLE.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(PREPARING_TABLE)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub PREPARING_TABLE_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(2), NameColumn(3),NameColumn(4), NameColumn(5))

		Dim MaxWidth As Int
		Dim MaxHeight As Int
		For i = 0 To PREPARING_TABLE.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
'			lbl.SetTextAlignment(Gravity.RIGHT,Gravity.CENTER)
			MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.Text, lbl.Font).Height + 10dip)
		Next
		
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
		
	Next
	
	For Each Column As B4XTableColumn In Array (NameColumn(3), NameColumn(4),NameColumn(5))

		For i = 0 To PREPARING_TABLE.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			lbl.Font = xui.CreateDefaultBoldFont(18)
		Next
	Next
	
	For i = 0 To PREPARING_TABLE.VisibleRowIds.Size - 1
		Dim RowId As Long = PREPARING_TABLE.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(0).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = PREPARING_TABLE.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(0).Id)
			If OtherColumnValue = ("NOT PREPARED") Then
				clr = xui.Color_Red
			Else If OtherColumnValue = ("PREPARED") Then
				clr = xui.Color_Green
			Else If OtherColumnValue = ("INCOMPLETE") Then
				clr = xui.Color_Cyan
			Else
				clr = xui.Color_Yellow
			End If
			pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Colors.RGB(215,215,215), 0)
			
		End If
	Next
	For Each Column As B4XTableColumn In Array (NameColumn(0))
		Column.InternalSortMode= "ASC"
	Next
	If ShouldRefresh Then
		PREPARING_TABLE.Refresh
		XSelections.Clear
	End If
End Sub
Sub PREPARING_TABLE_CellClicked (ColumnId As String, RowId As Long)
		XSelections.CellClicked(ColumnId, RowId)
		Dim RowData As Map = PREPARING_TABLE.GetRow(RowId)
		Dim ls As List
		ls.Initialize
		ls.Add("BARCODE NOT REGISTERED IN THE SYSTEM")
		ls.Add("NO ACTUAL BARCODE")
		ls.Add("NO SCANNER")
		ls.Add("SCANNER CAN READ BARCODE")
		ls.Add("DAMAGE BARCODE")
		ls.Add("FREE ITEM")
		If RowData.Get("Order") = "0" Then
			ls.Add("REMOVE SUGGESTION")
		Else
			ls.Add("OUT OF STOCK PARTIAL")
			ls.Add("OUT OF STOCK ALL (AUTO OS)")
		End If
		InputListAsync(ls, "Choose reason", -1, True) 'show list with paired devices
		Wait For InputList_Result (Result2 As Int)
		If Result2 <> DialogResponse.CANCEL Then
			reason = ls.Get(Result2)
			scan_code = "N/A"
			LABEL_LOAD_DESC_PREPARING.Text = RowData.Get("Product Description")
			CLEAR_PICKLIST_SKU
			Sleep(10)
			GET_PRODUCT_DETAILS
			Sleep(10)
			If ls.Get(Result2) = "OUT OF STOCK ALL (AUTO OS)" Then
				ProgressDialogShow2("Out of stock...",False)

				Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
				connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,RowData.Get("Unit")))
				Sleep(0)
				Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
				,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,RowData.Get("Unit"),RowData.Get("Order"),0,0 _
				,"ON HOLD",LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),"N/A",reason,LOGIN_MODULE.tab_id,0,picker))		
				Sleep(0)
				Dim unit As String = RowData.Get("Unit")
				If unit = "PCS" Then
					If RowData.Get("Order") > 1 Then
						unit = "PIECES"
					Else
						unit = "PIECE"
					End If
				Else If unit = "DOZ" Then
					unit = "DOZEN"
				End If
				TTS1.Speak(RowData.Get("Order") & " " & unit & " O.S",False)
				Sleep(100)
				LOAD_PREPARING_PICKLIST
				ProgressDialogHide()
				Sleep(0)
				ToastMessageShow("0 " & RowData.Get("Unit") & " PREPARED", False)
			Else If ls.Get(Result2) = "REMOVE SUGGESTION" Then
				Msgbox2Async("Are you sure you want to remove this suggestion?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
					connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,RowData.Get("Unit")))
					
					TTS1.Speak(RowData.Get("Order") & " " & RowData.Get("Unit") & " suggestion remove",False)
					Sleep(0)
					LOAD_PREPARING_PICKLIST
					Sleep(0)
					Dim unit As String = RowData.Get("Unit")
					If unit = "PCS" Then
						If RowData.Get("Served") > 1 Then
							unit = "PIECES"
						Else
							unit = "PIECE"
						End If
					Else If unit = "DOZ" Then
						unit = "DOZEN"
					End If
					ToastMessageShow(RowData.Get("Served") & " " & unit & " SUGGESTION REMOVE",False)
				Else
		
				End If
			Else
				ProgressDialogShow2("Loading...",False)
				scan_next_trigger = 1
				cursor4 = connection.ExecQuery("SELECT * FROM picklist_preparing_ref_table WHERE product_description ='"&LABEL_LOAD_DESC_PREPARING.Text&"' AND picklist_id = '"&picklist_id&"'")
				If cursor4.RowCount > 0 Then
					pcs_total_pieces = 0
					case_total_pieces = 0
					box_total_pieces = 0
					doz_total_pieces = 0
					pack_total_pieces = 0
					bag_total_pieces = 0
					For row = 0 To cursor4.RowCount - 1
						cursor4.Position = row
						LABEL_LOAD_VARIANT_PREPARING.Text = cursor4.GetString("product_variant")
						If cursor4.GetString("unit") = "CASE" Then
							LABEL_CASE_QTY_PREPARING.Text = cursor4.GetString("quantity")
							EDITTEXT_CASE_QTY_INPREPARED.Text = cursor4.GetString("quantity")
						End If
						If cursor4.GetString("unit") = "PCS" Then
							LABEL_PCS_QTY_PREPARING.Text = cursor4.GetString("quantity")
							EDITTEXT_PCS_QTY_INPREPARED.Text = cursor4.GetString("quantity")
						End If
						If cursor4.GetString("unit") = "BOX" Then
							LABEL_BOX_QTY_PREPARING.Text = cursor4.GetString("quantity")
							EDITTEXT_BOX_QTY_INPREPARED.Text = cursor4.GetString("quantity")
						End If
						If cursor4.GetString("unit") = "DOZ" Then
							LABEL_DOZ_QTY_PREPARING.Text = cursor4.GetString("quantity")
							EDITTEXT_DOZ_QTY_INPREPARED.Text = cursor4.GetString("quantity")
						End If
						If cursor4.GetString("unit") = "BAG" Then
							LABEL_BAG_QTY_PREPARING.Text = cursor4.GetString("quantity")
							EDITTEXT_BAG_QTY_INPREPARED.Text = cursor4.GetString("quantity")
						End If
						If cursor4.GetString("unit") = "PACK" Then
							LABEL_PACK_QTY_PREPARING.Text = cursor4.GetString("quantity")
							EDITTEXT_PACK_QTY_INPREPARED.Text = cursor4.GetString("quantity")
						End If
					Next	
					GET_TOTAL_SERVED
					Sleep(0)
					PANEL_BG_PREPARING.SetVisibleAnimated(300,True)
					PANEL_BG_PREPARING.BringToFront
					Sleep(0)
					ORDER_SPEECH
					ProgressDialogHide
				Else
					Msgbox2Async("The product you scanned :"& CRLF &""&LABEL_LOAD_DESC_PREPARING.Text&" "& CRLF &"IS NOT THE PICKLIST.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					CLEAR_PICKLIST_SKU
					Sleep(0)
					PANEL_BG_PREPARING.SetVisibleAnimated(300, False)
					
					
				End If
				
			End If
		Else
		End If
End Sub
Sub PREPARING_TABLE_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = PREPARING_TABLE.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)

End Sub

#Region BLUETOOTH
Sub ShowPairedDevices
	Dim PairedDevices As Map
	PairedDevices = serial1.GetPairedDevices
	Dim ls As List
	ls.Initialize
	For Iq = 0 To PairedDevices.Size - 1
		ls.Add(PairedDevices.GetKeyAt(Iq))
	Next
	If ls.Size=0 Then
		ls.Add("No device(s) found...")
	End If
	InputListAsync(ls, "Choose scanner", -1, True) 'show list with paired devices
	Wait For InputList_Result (Result As Int)
	If Result <> DialogResponse.CANCEL Then
		If ls.Get(Result)="No device(s) found..." Then
			Return
		Else
			ScannerMacAddress=PairedDevices.Get(ls.Get(Result)) 'convert the name to mac address and connect
			serial1.Connect(ScannerMacAddress)
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
			UpdateIcon("cart", cartBitmap)
		End If
	End If
End Sub
Sub Serial_Connected (success As Boolean)
	If success = True Then
		Log("Scanner is now connected. Waiting for data...")
		cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connected.png")
		UpdateIcon("cart", cartBitmap)
		ToastMessageShow("Scanner Connected", True)
		AStream.Initialize(serial1.InputStream, serial1.OutputStream, "AStream")
		ScannerOnceConnected=True
		Ts.Enabled=False
	Else
		If ScannerOnceConnected=False Then
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
			UpdateIcon("cart", cartBitmap)
			ToastMessageShow("Scanner is off, please turn on", False)
			ShowPairedDevices
		Else
			Log("Still waiting for the scanner to reconnect: " & ScannerMacAddress)
			Ts.Enabled=True
			cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
			UpdateIcon("cart", cartBitmap)
		End If
	End If
End Sub
Sub AStream_NewData (Buffer() As Byte)
	
	Log("Received: " & BytesToString(Buffer, 0, Buffer.Length, "UTF8"))
'	LABEL_LOAD_BARCODE.Text = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	If picklist_id = "" Then
		picklist_id = BytesToString(Buffer, 0, Buffer.Length, "UTF8").Trim
		VALIDATE_PICKLIST_STATUS
		Sleep(0)
		ENABLE_PICKLIST
	Else
#Region Scan Barcode
		If scan_next_trigger = 1 Then
			Msgbox2Async("You scan another item and will left the recent item with no transaction." &CRLF& "Are you sure you want to continue to the next item?","Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				scan_next_trigger = 0
			Else
				scan_next_trigger = 1
			End If
		End If
		Sleep(0)
		If scan_next_trigger = 0 Then
			CTRL.HideKeyboard
			Dim trigger As Int = 0
			cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") &"' as INTEGER) or case_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or box_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or bag_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or pack_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER)) and prod_status = '0' ORDER BY product_id")
			If cursor2.RowCount >= 2 Then
				Dim ls As List
				ls.Initialize
				Sleep(0)
				For row = 0 To cursor2.RowCount - 1
					cursor2.Position = row
					ls.Add(cursor2.GetString("product_desc"))
					DateTime.DateFormat = "yyyy-MM-dd"
				Next
				InputListAsync(ls, "Choose Description :", -1, True) 'show list with paired devices
				Wait For InputList_Result (Result As Int)
				If Result <> DialogResponse.CANCEL Then
					LABEL_LOAD_DESC_PREPARING.Text = ls.Get(Result)
					trigger = 0
				Else
					trigger = 1
				End If
				'SINGLE SKU
			Else if cursor2.RowCount = 1 Then
				For row = 0 To cursor2.RowCount - 1
					cursor2.Position = row
					Log(1)
					LABEL_LOAD_DESC_PREPARING.Text = cursor2.GetString("product_desc")
					trigger = 0
				Next
			Else
				Msgbox2Async("The barcode you scanned is not REGISTERED IN THE SYSTEM.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				trigger = 1
				CLEAR_PICKLIST_SKU
				Sleep(0)
				PANEL_BG_PREPARING.SetVisibleAnimated(300, False)
			End If
			Sleep(0)
			If trigger = 0 Then
				scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8").Trim
				Sleep(0)
				ProgressDialogShow2("Loading...",False)
				CLEAR_PICKLIST_SKU
				Sleep(0)
				GET_PRODUCT_DETAILS
				Sleep(100)
				cursor9 = connection.ExecQuery("SELECT * FROM picklist_preparing_ref_table WHERE product_description ='"&LABEL_LOAD_DESC_PREPARING.Text&"' AND picklist_id = '"&picklist_id&"'")
				If cursor9.RowCount > 0 Then
					pcs_total_pieces = 0
					case_total_pieces = 0
					box_total_pieces = 0
					doz_total_pieces = 0
					pack_total_pieces = 0
					bag_total_pieces = 0
					For row = 0 To cursor9.RowCount - 1
						cursor9.Position = row
						Sleep(0)
						LABEL_LOAD_VARIANT_PREPARING.Text = cursor9.GetString("product_variant")
						If cursor9.GetString("unit") = "CASE" Then
							LABEL_CASE_QTY_PREPARING.Text = cursor9.GetString("quantity")
							EDITTEXT_CASE_QTY_INPREPARED.Text = cursor9.GetString("quantity")
						End If
						If cursor9.GetString("unit") = "PCS" Then
							LABEL_PCS_QTY_PREPARING.Text = cursor9.GetString("quantity")
							EDITTEXT_PCS_QTY_INPREPARED.Text = cursor9.GetString("quantity")
						End If
						If cursor9.GetString("unit") = "BOX" Then
							LABEL_BOX_QTY_PREPARING.Text = cursor9.GetString("quantity")
							EDITTEXT_BOX_QTY_INPREPARED.Text = cursor9.GetString("quantity")
						End If
						If cursor9.GetString("unit") = "DOZ" Then
							LABEL_DOZ_QTY_PREPARING.Text = cursor9.GetString("quantity")
							EDITTEXT_DOZ_QTY_INPREPARED.Text = cursor9.GetString("quantity")
						End If
						If cursor9.GetString("unit") = "BAG" Then
							LABEL_BAG_QTY_PREPARING.Text = cursor9.GetString("quantity")
							EDITTEXT_BAG_QTY_INPREPARED.Text = cursor9.GetString("quantity")
						End If
						If cursor9.GetString("unit") = "PACK" Then
							LABEL_PACK_QTY_PREPARING.Text = cursor9.GetString("quantity")
							EDITTEXT_PACK_QTY_INPREPARED.Text = cursor9.GetString("quantity")
						End If
					Next		
					GET_TOTAL_SERVED
					reason = "N/A"
					Sleep(0)
					ORDER_SPEECH
					Sleep(0)
					ProgressDialogHide
					Sleep(100)
					PANEL_BG_PREPARING.SetVisibleAnimated(300,True)
					PANEL_BG_PREPARING.BringToFront
				Else
					PANEL_BG_PREPARING.SetVisibleAnimated(300, False)
					Msgbox2Async("The product you scanned :"& CRLF &""&LABEL_LOAD_DESC_PREPARING.Text&" "& CRLF &"IS NOT THE PICKLIST.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					Wait For Msgbox_Result (Result As Int)
'					If Result = DialogResponse.CANCEL Then
'
'					Else
						Sleep(0)
						PANEL_BG_TRAIL.SetVisibleAnimated(300,True)
						Sleep(0)
						LOAD_REASON
						GET_UNIT
						CMB_PICKER.SelectedIndex = -1
						Sleep(0)
						OpenSpinner(CMB_PICKER.cmbBox)
						ProgressDialogHide
'					End If
				End If
			End If
			Sleep(0)
			scan_next_trigger = 1
		End If
#end Region
	End If
End Sub
Sub AStream_Error
	Log("Connection broken...")
	AStream.Close
	serial1.Disconnect
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	UpdateIcon("cart", cartBitmap)
	If ScannerOnceConnected=True Then
		Ts.Enabled=True
		cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connected.png")
		UpdateIcon("cart", cartBitmap)
	Else
		ShowPairedDevices
	End If
End Sub
Sub AStream_Terminated
	Log("Connection terminated...")
	AStream_Error
End Sub
Sub Timer_Tick
	Ts.Enabled=False
	serial1.Connect(ScannerMacAddress)
	Log ("Trying to connect...")
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
	UpdateIcon("cart", cartBitmap)
End Sub
#End Region

Sub GET_PRODUCT_DETAILS
	cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESC_PREPARING.Text&"'")
	If cursor3.RowCount > 0 Then
		For qrow = 0 To cursor3.RowCount - 1
			cursor3.Position = qrow
			Sleep(0)
			LABEL_LOAD_VARIANT_PREPARING.Text = cursor3.GetString("product_variant")
			principal_id = cursor3.GetString("principal_id")
			product_id = cursor3.GetString("product_id")
			
			caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
			pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
			dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
			boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
			bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
			packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
			If caseper = 0 Then
				BUTTON_CASE_PREPARING.Enabled = False
				BUTTON_CASE_PREPARING.Background = BG_DISABLE
			Else
				BUTTON_CASE_PREPARING.Enabled = True
				BUTTON_CASE_PREPARING.Background = BG_ENABLE
			End If
			If pcsper = 0 Then
				BUTTON_PCS_PREPARING.Enabled = False
				BUTTON_PCS_PREPARING.Background = BG_DISABLE
			Else
				BUTTON_PCS_PREPARING.Enabled = True
				BUTTON_PCS_PREPARING.Background = BG_ENABLE
			End If
			If boxper = 0 Then
				BUTTON_BOX_PREPARING.Enabled = False
				BUTTON_BOX_PREPARING.Background = BG_DISABLE
			Else
				BUTTON_BOX_PREPARING.Enabled = True
				BUTTON_BOX_PREPARING.Background = BG_ENABLE
			End If
			If dozper = 0 Then
				BUTTON_DOZ_PREPARING.Enabled = False
				BUTTON_DOZ_PREPARING.Background = BG_DISABLE
			Else
				BUTTON_DOZ_PREPARING.Enabled = True
				BUTTON_DOZ_PREPARING.Background = BG_ENABLE
			End If
			If packper = 0 Then
				BUTTON_PACK_PREPARING.Enabled = False
				BUTTON_PACK_PREPARING.Background = BG_DISABLE
			Else
				BUTTON_PACK_PREPARING.Enabled = True
				BUTTON_PACK_PREPARING.Background = BG_ENABLE
			End If
			If bagper = 0 Then
				BUTTON_BAG_PREPARING.Enabled = False
				BUTTON_BAG_PREPARING.Background = BG_DISABLE
			Else
				BUTTON_BAG_PREPARING.Enabled = True
				BUTTON_BAG_PREPARING.Background = BG_ENABLE
			End If
			
			If scan_code = cursor3.GetString("case_bar_code") Then
				LABEL_LOAD_SCANCODE.Text = "CASE"
			Else If scan_code = cursor3.GetString("bar_code") Then
				LABEL_LOAD_SCANCODE.Text = "PCS"
			Else If scan_code = cursor3.GetString("box_bar_code") Then
				LABEL_LOAD_SCANCODE.Text = "BOX"
			Else If scan_code = cursor3.GetString("pack_bar_code") Then
				LABEL_LOAD_SCANCODE.Text = "PACK"
			Else If scan_code = cursor3.GetString("bag_bar_code") Then
				LABEL_LOAD_SCANCODE.Text = "CASE"
			Else
				LABEL_LOAD_SCANCODE.Text = "-"
			End If
		Next	
		cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
		If cursor6.RowCount > 0 Then
			For row = 0 To cursor6.RowCount - 1
				cursor6.Position = row
				principal_name = cursor6.Getstring("principal_name")
			Next		
		End If
		Sleep(0)
	End If
End Sub
Sub GET_TOTAL_ORDER
	cursor16 = connection.ExecQuery("SELECT sum(total_pieces) as 'total_pieces' FROM picklist_preparing_ref_table WHERE product_description ='"&LABEL_LOAD_DESC_PREPARING.Text&"' and picklist_id = '"&picklist_id&"'")
	If cursor16.RowCount > 0 Then
		For row = 0 To cursor16.RowCount - 1
			cursor16.Position = row
			total_order = cursor16.GetString("total_pieces")
		Next	
	End If
End Sub
Sub GET_TOTAL_SERVED
	cursor7 = connection.ExecQuery("SELECT * FROM picklist_preparing_disc_table WHERE product_description ='"&LABEL_LOAD_DESC_PREPARING.Text&"' AND picklist_id = '"&picklist_id&"'")
	If cursor7.RowCount > 0 Then
		For rowq = 0 To cursor7.RowCount - 1
			cursor7.Position = rowq
			Sleep(100)
			If cursor7.GetString("unit") = "CASE" Then
				EDITTEXT_CASE_QTY_INPREPARED.Text = cursor7.GetString("prepared_quantity")
				case_total_pieces = cursor7.GetString("prepared_total_pcs")
				BUTTON_CASE_PREPARING.Text = "UPDATE"
				BUTTON_CASE_PREPARING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "PCS" Then
				EDITTEXT_PCS_QTY_INPREPARED.Text = cursor7.GetString("prepared_quantity")
				pcs_total_pieces = cursor7.GetString("prepared_total_pcs")
				BUTTON_PCS_PREPARING.Text = "UPDATE"
				BUTTON_PCS_PREPARING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "BOX" Then
				EDITTEXT_BOX_QTY_INPREPARED.Text = cursor7.GetString("prepared_quantity")
				box_total_pieces = cursor7.GetString("prepared_total_pcs")
				BUTTON_BOX_PREPARING.Text = "UPDATE"
				BUTTON_BOX_PREPARING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "DOZ" Then
				EDITTEXT_DOZ_QTY_INPREPARED.Text = cursor7.GetString("prepared_quantity")
				doz_total_pieces = cursor7.GetString("prepared_total_pcs")
				BUTTON_DOZ_PREPARING.Text = "UPDATE"
				BUTTON_DOZ_PREPARING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "BAG" Then
				EDITTEXT_BAG_QTY_INPREPARED.Text = cursor7.GetString("prepared_quantity")
				bag_total_pieces = cursor7.GetString("prepared_total_pcs")
				BUTTON_BAG_PREPARING.Text = "UPDATE"
				BUTTON_BAG_PREPARING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "PACK" Then
				EDITTEXT_PACK_QTY_INPREPARED.Text = cursor7.GetString("prepared_quantity")
				pack_total_pieces = cursor7.GetString("prepared_total_pcs")
				BUTTON_PACK_PREPARING.Text = "UPDATE"
				BUTTON_PACK_PREPARING.Background = BG_UPDATE
			End If
		Next
	End If
End Sub
Sub CLEAR_PICKLIST_SKU
	LABEL_CASE_QTY_PREPARING.Text = "0"
	LABEL_PCS_QTY_PREPARING.Text = "0"
	LABEL_BOX_QTY_PREPARING.Text = "0"
	LABEL_DOZ_QTY_PREPARING.Text = "0"
	LABEL_BAG_QTY_PREPARING.Text = "0"
	LABEL_PACK_QTY_PREPARING.Text = "0"
	EDITTEXT_CASE_QTY_INPREPARED.Text = ""
	EDITTEXT_PCS_QTY_INPREPARED.Text = ""
	EDITTEXT_BOX_QTY_INPREPARED.Text = ""
	EDITTEXT_DOZ_QTY_INPREPARED.Text = ""
	EDITTEXT_PACK_QTY_INPREPARED.Text = ""
	EDITTEXT_BAG_QTY_INPREPARED.Text = ""
	BUTTON_BAG_PREPARING.Text = "PREPARE"
	BUTTON_BOX_PREPARING.Text = "PREPARE"
	BUTTON_CASE_PREPARING.Text = "PREPARE"
	BUTTON_DOZ_PREPARING.Text = "PREPARE"
	BUTTON_PACK_PREPARING.Text = "PREPARE"
	BUTTON_PCS_PREPARING.Text = "PREPARE"
	BUTTON_CASE_PREPARING.Enabled = False
	BUTTON_CASE_PREPARING.Background = BG_DISABLE
	BUTTON_PCS_PREPARING.Enabled = False
	BUTTON_PCS_PREPARING.Background = BG_DISABLE
	BUTTON_BOX_PREPARING.Enabled = False
	BUTTON_BOX_PREPARING.Background = BG_DISABLE
	BUTTON_DOZ_PREPARING.Enabled = False
	BUTTON_DOZ_PREPARING.Background = BG_DISABLE
	BUTTON_PACK_PREPARING.Enabled = False
	BUTTON_PACK_PREPARING.Background = BG_DISABLE
	BUTTON_BAG_PREPARING.Enabled = False
	BUTTON_BAG_PREPARING.Background = BG_DISABLE

End Sub
'process
Sub PREPARED_PCS
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_PCS_QTY_INPREPARED.Text * pcsper
	
	Dim prepared_status As String
	
	If EDITTEXT_PCS_QTY_INPREPARED.Text <> LABEL_PCS_QTY_PREPARING.Text Then
		If EDITTEXT_PCS_QTY_INPREPARED.Text = 0 Or LABEL_PCS_QTY_PREPARING.Text = 0  Then
			prepared_status = "ON HOLD"
		Else
			Msgbox2Async("Is this a OS or a PARTIAL  QUANTITY?" , "", "OS", "", "PARTIAL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				prepared_status = "ON HOLD"
			Else
				prepared_status = "INCOMPLETE"
			End If
		End If
	Else
		prepared_status = "PREPARED"
	End If
	Dim SPEECH1 As String
	If BUTTON_PCS_PREPARING.Text = "PREPARE" Then
		SPEECH1 = "PREPARED"
	Else
		SPEECH1 = "UPDATED"
	End If
	
	Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,"PCS"))
	Sleep(10)
	Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,"PCS",LABEL_PCS_QTY_PREPARING.Text,EDITTEXT_PCS_QTY_INPREPARED.Text,total_pieces _
	,prepared_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0,picker))
	ToastMessageShow(EDITTEXT_PCS_QTY_INPREPARED.Text & " " & "PIECE(S) " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_PCS_QTY_INPREPARED.Text & " " & "PIECES " & SPEECH1, False)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub PREPARED_CASE
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_CASE_QTY_INPREPARED.Text * caseper
	
	Dim prepared_status As String
	
	If EDITTEXT_CASE_QTY_INPREPARED.Text <> LABEL_CASE_QTY_PREPARING.Text Then
		If EDITTEXT_CASE_QTY_INPREPARED.Text = 0 Or LABEL_CASE_QTY_PREPARING.Text = 0  Then
			prepared_status = "ON HOLD"
		Else
			Msgbox2Async("Is this a OS or a PARTIAL  QUANTITY?" , "SHORT SERVING", "OS", "", "PARTIAL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				prepared_status = "ON HOLD"
			Else
				prepared_status = "INCOMPLETE"
			End If
		End If
	Else
		prepared_status = "PREPARED"
	End If
	
	Dim SPEECH1 As String
	If BUTTON_CASE_PREPARING.Text = "PREPARE" Then
		SPEECH1 = "PREPARED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,"CASE"))
	Sleep(10)
	Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,"CASE",LABEL_CASE_QTY_PREPARING.Text,EDITTEXT_CASE_QTY_INPREPARED.Text,total_pieces _
	,prepared_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0,picker))
	ToastMessageShow(EDITTEXT_CASE_QTY_INPREPARED.Text & " " & "CASE " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_CASE_QTY_INPREPARED.Text & " " & "CASE " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub PREPARED_BOX
	ProgressDialogShow2("Loading...", False)
	scan_next_trigger = 0
	total_pieces = EDITTEXT_BOX_QTY_INPREPARED.Text * boxper
	
	Dim prepared_status As String
	
	If EDITTEXT_BOX_QTY_INPREPARED.Text <> LABEL_BOX_QTY_PREPARING.Text Then
		If EDITTEXT_BOX_QTY_INPREPARED.Text = 0 Or LABEL_BOX_QTY_PREPARING.Text = 0  Then
			prepared_status = "ON HOLD"
		Else
			Msgbox2Async("Is this a OS or a PARTIAL  QUANTITY?" , "", "OS", "", "PARTIAL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				prepared_status = "ON HOLD"
			Else
				prepared_status = "INCOMPLETE"
			End If
		End If
	Else
		prepared_status = "PREPARED"
	End If
	Dim SPEECH1 As String
	If BUTTON_BOX_PREPARING.Text = "PREPARE" Then
		SPEECH1 = "PREPARED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,"BOX"))
	Sleep(10)
	Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,"BOX",LABEL_BOX_QTY_PREPARING.Text,EDITTEXT_BOX_QTY_INPREPARED.Text,total_pieces _
	,prepared_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0,picker))
	ToastMessageShow(EDITTEXT_BOX_QTY_INPREPARED.Text & " " & "BOX " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_BOX_QTY_INPREPARED.Text & " " & "BOX " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub PREPARED_DOZ
	ProgressDialogShow2("Loading...", False)
	scan_next_trigger = 0
	total_pieces = EDITTEXT_DOZ_QTY_INPREPARED.Text * dozper
	
	Dim prepared_status As String
	
	If EDITTEXT_DOZ_QTY_INPREPARED.Text <> LABEL_DOZ_QTY_PREPARING.Text Then
		If EDITTEXT_DOZ_QTY_INPREPARED.Text = 0 Or LABEL_DOZ_QTY_PREPARING.Text = 0  Then
			prepared_status = "ON HOLD"
		Else
			Msgbox2Async("Is this a OS or a PARTIAL  QUANTITY?" , "", "OS", "", "PARTIAL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				prepared_status = "ON HOLD"
			Else
				prepared_status = "INCOMPLETE"
			End If
		End If
	Else
		prepared_status = "PREPARED"
	End If
	Dim SPEECH1 As String
	If BUTTON_DOZ_PREPARING.Text = "PREPARE" Then
		SPEECH1 = "PREPARED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,"DOZ"))
	Sleep(10)
	Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,"DOZ",LABEL_DOZ_QTY_PREPARING.Text,EDITTEXT_DOZ_QTY_INPREPARED.Text,total_pieces _
	,prepared_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0,picker))
	ToastMessageShow(EDITTEXT_DOZ_QTY_INPREPARED.Text & " " & "DOZ " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_DOZ_QTY_INPREPARED.Text & " " & "DOZ " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub PREPARED_PACK
	ProgressDialogShow2("Loading...", False)
	scan_next_trigger = 0
	total_pieces = EDITTEXT_PACK_QTY_INPREPARED.Text * packper
	
	Dim prepared_status As String
	
	If EDITTEXT_PACK_QTY_INPREPARED.Text <> LABEL_PACK_QTY_PREPARING.Text Then
		If EDITTEXT_PACK_QTY_INPREPARED.Text = 0 Or LABEL_PACK_QTY_PREPARING.Text = 0  Then
			prepared_status = "ON HOLD"
		Else
			Msgbox2Async("Is this a OS or a PARTIAL  QUANTITY?" , "", "OS", "", "PARTIAL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				prepared_status = "ON HOLD"
			Else
				prepared_status = "INCOMPLETE"
			End If
		End If
	Else
		prepared_status = "PREPARED"
	End If
	Dim SPEECH1 As String
	If BUTTON_PACK_PREPARING.Text = "PREPARE" Then
		SPEECH1 = "PREPARED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,"PACK"))
	Sleep(10)
	Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,"PACK",LABEL_PACK_QTY_PREPARING.Text,EDITTEXT_PACK_QTY_INPREPARED.Text,total_pieces _
	,prepared_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0,picker))
	ToastMessageShow(EDITTEXT_PACK_QTY_INPREPARED.Text & " " & "PACK " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_PACK_QTY_INPREPARED.Text & " " & "PACK " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub PREPARED_BAG
	ProgressDialogShow2("Loading...", False)
	scan_next_trigger = 0
	total_pieces = EDITTEXT_BAG_QTY_INPREPARED.Text * bagper
	
	Dim prepared_status As String
	
	If EDITTEXT_BAG_QTY_INPREPARED.Text <> LABEL_BAG_QTY_PREPARING.Text Then
		If EDITTEXT_BAG_QTY_INPREPARED.Text = 0 Or LABEL_BAG_QTY_PREPARING.Text = 0  Then
			prepared_status = "ON HOLD"
		Else
			Msgbox2Async("Is this a OS or a PARTIAL  QUANTITY?" , "", "OS", "", "PARTIAL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				prepared_status = "ON HOLD"
			Else
				prepared_status = "INCOMPLETE"
			End If
		End If
	Else
		prepared_status = "PREPARED"
	End If
	
	Dim SPEECH1 As String
	If BUTTON_BAG_PREPARING.Text = "PREPARE" Then
		SPEECH1 = "PREPARED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_preparing_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_PREPARING.Text,"BAG"))
	Sleep(10)
	Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,"BAG",LABEL_BAG_QTY_PREPARING.Text,EDITTEXT_BAG_QTY_INPREPARED.Text,total_pieces _
	,prepared_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0,picker))
	ToastMessageShow(EDITTEXT_BAG_QTY_INPREPARED.Text & " " & "BAG " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_BAG_QTY_INPREPARED.Text & " " & "BAG " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
'buttonclick
Sub BUTTON_PCS_PREPARING_Click
	GET_TOTAL_ORDER
	Sleep(0)
	If EDITTEXT_PCS_QTY_INPREPARED.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you preparing, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		total_serve = (EDITTEXT_PCS_QTY_INPREPARED.Text * pcsper) + case_total_pieces + box_total_pieces + doz_total_pieces + pack_total_pieces + bag_total_pieces
		If total_serve > total_order Then
			Msgbox2Async("The total pieces you serving is OVER to the total pieces that ordered in this picklist.", "Over Serving!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			If LABEL_PCS_QTY_PREPARING.Text = "0" Then
				Msgbox2Async("Are you sure to suggest this unit-quantity to the picklist?", "Picklist Suggestion", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PREPARED_PCS
				End If
			Else
				PREPARED_PCS
			End If
		End If
	End If
End Sub
Sub BUTTON_CASE_PREPARING_Click
	GET_TOTAL_ORDER
	Sleep(0)
	If EDITTEXT_CASE_QTY_INPREPARED.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you preparing, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		total_serve = (EDITTEXT_CASE_QTY_INPREPARED.Text * caseper) + pcs_total_pieces + box_total_pieces + doz_total_pieces + pack_total_pieces + bag_total_pieces
		If total_serve > total_order Then
			Msgbox2Async("The total pieces you serving is OVER to the total pieces that ordered in this picklist.", "Over Serving!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			If LABEL_CASE_QTY_PREPARING.Text = "0" Then
				Msgbox2Async("Are you sure to suggest this unit-quantity to the picklist?", "Picklist Suggestion", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PREPARED_CASE
				End If
			Else
				PREPARED_CASE
			End If
		End If
	End If
End Sub
Sub BUTTON_BOX_PREPARING_Click
	GET_TOTAL_ORDER
	Sleep(0)
	If EDITTEXT_BOX_QTY_INPREPARED.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you preparing, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		total_serve = (EDITTEXT_BOX_QTY_INPREPARED.Text * boxper) + pcs_total_pieces + case_total_pieces + doz_total_pieces + pack_total_pieces + bag_total_pieces
		If total_serve > total_order Then
			Msgbox2Async("The total pieces you serving is OVER to the total pieces that ordered in this picklist.", "Over Serving!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			If LABEL_BOX_QTY_PREPARING.Text = "0" Then
				Msgbox2Async("Are you sure to suggest this unit-quantity to the picklist?", "Picklist Suggestion", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PREPARED_BOX
				End If
			Else
				PREPARED_BOX
			End If
		End If
	End If
End Sub
Sub BUTTON_DOZ_PREPARING_Click
	GET_TOTAL_ORDER
	Sleep(0)
	If EDITTEXT_DOZ_QTY_INPREPARED.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you preparing, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		total_serve = (EDITTEXT_DOZ_QTY_INPREPARED.Text * dozper) + pcs_total_pieces + box_total_pieces + case_total_pieces + pack_total_pieces + bag_total_pieces
		If total_serve > total_order Then
			Msgbox2Async("The total pieces you serving is OVER to the total pieces that ordered in this picklist.", "Over Serving!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			If LABEL_DOZ_QTY_PREPARING.Text = "0" Then
				Msgbox2Async("Are you sure to suggest this unit-quantity to the picklist?", "Picklist Suggestion", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PREPARED_DOZ
				End If
			Else
				PREPARED_DOZ
			End If
		End If
	End If
End Sub
Sub BUTTON_PACK_PREPARING_Click
	GET_TOTAL_ORDER
	Sleep(0)
	If EDITTEXT_PACK_QTY_INPREPARED.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you preparing, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		total_serve = (EDITTEXT_PACK_QTY_INPREPARED.Text * packper) + pcs_total_pieces + box_total_pieces + doz_total_pieces + case_total_pieces + bag_total_pieces
		If total_serve > total_order Then
			Msgbox2Async("The total pieces you serving is OVER to the total pieces that ordered in this picklist.", "Over Serving!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			If LABEL_PACK_QTY_PREPARING.Text = "0" Then
				Msgbox2Async("Are you sure to suggest this unit-quantity to the picklist?", "Picklist Suggestion", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PREPARED_PACK
				End If
			Else
				PREPARED_PACK
			End If
		End If
	End If
End Sub
Sub BUTTON_BAG_PREPARING_Click
	GET_TOTAL_ORDER
	Sleep(0)
	If EDITTEXT_BAG_QTY_INPREPARED.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you preparing, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		total_serve = (EDITTEXT_BAG_QTY_INPREPARED.Text * bagper) + pcs_total_pieces + box_total_pieces + doz_total_pieces + pack_total_pieces + case_total_pieces
		If total_serve > total_order Then
			Msgbox2Async("The total pieces you serving is OVER to the total pieces that ordered in this picklist.", "Over Serving!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Else
			If LABEL_BAG_QTY_PREPARING.Text = "0" Then
				Msgbox2Async("Are you sure to suggest this unit-quantity to the picklist?", "Picklist Suggestion", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PREPARED_BAG
				End If
			Else
				PREPARED_BAG
			End If
		End If
	End If
End Sub
'focuschange
Sub EDITTEXT_BAG_QTY_INPREPARED_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_BAG_QTY_INPREPARED.SelectAll
	Else
	End If
End Sub
Sub EDITTEXT_PACK_QTY_INPREPARED_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_PACK_QTY_INPREPARED.SelectAll
	Else
	End If
End Sub
Sub EDITTEXT_DOZ_QTY_INPREPARED_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_DOZ_QTY_INPREPARED.SelectAll
	Else
		
	End If
End Sub
Sub EDITTEXT_BOX_QTY_INPREPARED_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_BOX_QTY_INPREPARED.SelectAll
	Else
		
	End If
End Sub
Sub EDITTEXT_CASE_QTY_INPREPARED_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_CASE_QTY_INPREPARED.SelectAll
	Else
		
	End If
End Sub
Sub EDITTEXT_PCS_QTY_INPREPARED_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_PCS_QTY_INPREPARED.SelectAll
	Else
		
	End If
End Sub

Sub BUTTON_EXIT_PREPARING_Click
	scan_next_trigger = 0
	CLEAR_PICKLIST_SKU
	Sleep(0)
	PANEL_BG_PREPARING.SetVisibleAnimated(300, False)
	CTRL.HideKeyboard
	XSelections.Clear
	Sleep(0)
	LOAD_PREPARING_PICKLIST
End Sub
Sub PANEL_BG_PREPARING_Click
	Return True
End Sub

Sub TTS1_Ready (Success As Boolean)
	If Success Then
		'enable all views
'		Cell_Click 'play first sentence
	Else
'		Msgbox("Error initializing TTS engine.", "")
	End If
End Sub
Sub ORDER_SPEECH
	Dim PREPARING_SPEECH_PCS As String
	Dim PREPARING_SPEECH_CASE As String
	Dim PREPARING_SPEECH_BOX As String
	Dim PREPARING_SPEECH_DOZ As String
	Dim PREPARING_SPEECH_PACK As String
	Dim PREPARING_SPEECH_BAG As String

	If LABEL_CASE_QTY_PREPARING.Text > 0 Then
		If LABEL_PCS_QTY_PREPARING.Text > 0 Or _
						LABEL_BOX_QTY_PREPARING.Text > 0 Or _
						LABEL_DOZ_QTY_PREPARING.Text > 0 Or _
						LABEL_PACK_QTY_PREPARING.Text > 0 Or _
						LABEL_BAG_QTY_PREPARING.Text > 0 Then
			PREPARING_SPEECH_CASE = LABEL_CASE_QTY_PREPARING.Text & " CASE and "
		Else
			PREPARING_SPEECH_CASE = LABEL_CASE_QTY_PREPARING.Text & " CASE. "
		End If
	Else
		PREPARING_SPEECH_CASE = " "
	End If
	Sleep(0)
	If LABEL_PCS_QTY_PREPARING.Text > 0 Then
		If LABEL_BOX_QTY_PREPARING.Text > 0 Or _
						LABEL_DOZ_QTY_PREPARING.Text > 0 Or _
						LABEL_PACK_QTY_PREPARING.Text > 0 Or _
						LABEL_BAG_QTY_PREPARING.Text > 0 Then
			PREPARING_SPEECH_PCS = LABEL_PCS_QTY_PREPARING.Text & " PIECES and "
		Else
			PREPARING_SPEECH_PCS = LABEL_PCS_QTY_PREPARING.Text & " PIECES. "
		End If
	Else
		PREPARING_SPEECH_PCS = " "
	End If
	Sleep(0)
	If LABEL_BOX_QTY_PREPARING.Text > 0 Then
				
		If LABEL_DOZ_QTY_PREPARING.Text > 0 Or _
						LABEL_PACK_QTY_PREPARING.Text > 0 Or _
						LABEL_BAG_QTY_PREPARING.Text > 0 Then
			PREPARING_SPEECH_BOX = LABEL_BOX_QTY_PREPARING.Text & " BOX and "
		Else
			PREPARING_SPEECH_BOX = LABEL_BOX_QTY_PREPARING.Text & " BOX. "
		End If
	Else
		PREPARING_SPEECH_BOX = " "
	End If
	Sleep(0)
	If LABEL_DOZ_QTY_PREPARING.Text > 0 Then
		If LABEL_PACK_QTY_PREPARING.Text > 0 Or _
						LABEL_BAG_QTY_PREPARING.Text > 0 Then
			PREPARING_SPEECH_DOZ = LABEL_DOZ_QTY_PREPARING.Text & " DOZEN and "
		Else
			PREPARING_SPEECH_DOZ = LABEL_DOZ_QTY_PREPARING.Text & " DOZEN. "
		End If
	Else
		PREPARING_SPEECH_DOZ = " "
	End If
	Sleep(0)
	If LABEL_PACK_QTY_PREPARING.Text > 0 Then
		If LABEL_BAG_QTY_PREPARING.Text > 0 Then
			PREPARING_SPEECH_PACK = LABEL_PACK_QTY_PREPARING.Text & " PACK and "
		Else
			PREPARING_SPEECH_PACK = LABEL_PACK_QTY_PREPARING.Text & " PACK. "
		End If
	Else
		PREPARING_SPEECH_PACK = " "
	End If
	Sleep(0)
	If LABEL_BAG_QTY_PREPARING.Text > 0 Then
		PREPARING_SPEECH_BAG = LABEL_BAG_QTY_PREPARING.Text & " BAG. "
	Else
		PREPARING_SPEECH_BAG = " "
	End If
	Sleep(20)
	TTS1.Speak(PREPARING_SPEECH_CASE _
					 & PREPARING_SPEECH_PCS _
					 & PREPARING_SPEECH_BOX _
					  & PREPARING_SPEECH_DOZ _
					  & PREPARING_SPEECH_PACK _
					   & PREPARING_SPEECH_BAG, True)
	Sleep(20)
	TTS1.Speak(PREPARING_SPEECH_CASE & PREPARING_SPEECH_PCS & PREPARING_SPEECH_BOX & PREPARING_SPEECH_DOZ & PREPARING_SPEECH_PACK & PREPARING_SPEECH_BAG, True)
End Sub

Sub GET_STATUS
	Dim order_count As Int
	Dim serve_count As Int
	Dim on_hold_count As Int
	cursor12 = connection.ExecQuery("SELECT COUNT(product_id) as 'order_count' FROM picklist_preparing_ref_table WHERE picklist_id = '"&picklist_id&"' GROUP BY product_id, unit")
	If cursor12.RowCount > 0 Then
		For ia = 0 To cursor12.RowCount - 1
			Sleep(0)
			cursor12.Position = ia
			order_count = order_count + 1
		Next	
	Else
		order_count = 0		
	End If	
	cursor13 = connection.ExecQuery("SELECT COUNT(product_id) as 'serve_count' FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id&"' and prepared_status <> 'INCOMPLETE' GROUP BY product_id, unit")
	If cursor13.RowCount > 0 Then
		For ib = 0 To cursor13.RowCount - 1
			cursor13.Position = ib
			serve_count = serve_count + 1
		Next			
	Else
		serve_count = 0		
	End If	
	LABEL_LOAD_STATUS.Text = serve_count & " / " & order_count
	If serve_count >= order_count Then
		LABEL_LOAD_STATUS.Color = Colors.Green
		BUTTON_PREPARE.Enabled = True
	Else
		LABEL_LOAD_STATUS.Color = Colors.Red
		BUTTON_PREPARE.Enabled = False
	End If	
	cursor14 = connection.ExecQuery("SELECT COUNT(product_id) as 'on_hold' FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id&"' AND prepared_status = 'ON HOLD' GROUP BY product_id, unit")
	If cursor14.RowCount > 0 Then
		For ib = 0 To cursor14.RowCount - 1
			cursor14.Position = ib
			on_hold_count = on_hold_count + 1
		Next	
	Else
		on_hold_count = 0
	End If
	LABEL_LOAD_ONHOLD.Text = on_hold_count
End Sub

Sub LOAD_PICKER
	Dim pickers As Map
	Dim name As List
	pickers.Initialize
	name.Initialize
	
	cursor8 = connection.ExecQuery("SELECT User FROM users_table WHERE Position LIKE '%LOGISTIC PERSONEL%' ORDER BY User ASC")
	For i = 0 To cursor8.RowCount - 1
		Sleep(0)
		cursor8.Position = i
		pickers.Put(cursor8.GetString("User"), False)
		name.Add(cursor8.GetString("User"))
	Next
	CMB_PICKER.cmbBox.Clear
	InputMapAsync(pickers, "SELECT PICKER", False)
	Wait For InputMap_Result
	Dim check As String
	For i = 0 To pickers.Size - 1
		If pickers.GetValueAt(i) = True Then
			check = check & name.Get(i) & " / "
			CMB_PICKER.cmbBox.Add(name.Get(i))
		End If
	Next
'	For Each check As String In pickers.Keys
'		If pickers.Get(check)=True Then
'			check1 = check1 & check & " / "
'		End If
'	Next
	If check = "" Then
		LOAD_PICKER
	Else
		picker = check.SubString2(0, check.Length - 3)
		Log(picker)
	End If
End Sub
Sub BUTTON_PICKER_Click
	LOAD_PICKER
End Sub

Sub BUTTON_AUTOFILL_Click
	GET_SECURITY
	Sleep(0)
	PANEL_BG_SECURITY.SetVisibleAnimated(300, True)
	PANEL_BG_SECURITY.BringToFront
	EDITTEXT_PASSWORD.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_PASSWORD)
	security_trigger = "AUTO FILL"
End Sub
Sub AUTO_FILL
	cursor4 = connection.ExecQuery("SELECT * FROM picklist_preparing_ref_table WHERE picklist_id = '"&picklist_id&"'")
	If cursor4.RowCount > 0 Then
		PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
		EDITTEXT_PASSWORD.Text = ""
		CTRL.HideKeyboard
		Sleep(0)
		connection.ExecNonQuery("DELETE FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id.Trim&"' and quantity = '0'")
		ProgressDialogShow2("Auto Filling...", False)
		For ia = 0 To cursor4.RowCount - 1
			Sleep(0)
			cursor4.Position = ia
			cursor5 = connection.ExecQuery("SELECT * FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id&"' AND product_description = '"&cursor4.GetString("product_description")&"' AND unit = '"&cursor4.GetString("unit")&"'")
			If cursor5.RowCount > 0 Then
			Else
				LABEL_LOAD_DESC_PREPARING.text = cursor4.GetString("product_description")
				GET_PRODUCT_DETAILS
				Sleep(0)
				Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
				,cursor4.GetString("product_id"),cursor4.GetString("product_variant"),cursor4.GetString("product_description"),cursor4.GetString("unit"),cursor4.GetString("quantity"),cursor4.GetString("quantity"),cursor4.GetString("total_pieces") _
				,"PREPARED",LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),"AUTO FILL " & CMB_ACCOUNT.cmbBox.SelectedItem,"AUTO FILL " & CMB_ACCOUNT.cmbBox.SelectedItem,LOGIN_MODULE.tab_id,0,picker))
			End If
		Next
		ToastMessageShow("Auto Filling Succesfull", False)
		Sleep(0)
		LOAD_PREPARING_PICKLIST
		Sleep(1000)
		ProgressDialogHide()
	Else	
	End If
End Sub
Sub GET_SECURITY
	CMB_ACCOUNT.cmbBox.Clear
	cursor3 = connection.ExecQuery("SELECT User FROM users_table WHERE Position LIKE '%LOGISTIC OFFICER%' ORDER BY User ASC")
	For i = 0 To cursor3.RowCount - 1
		Sleep(0)
		cursor3.Position = i
		CMB_ACCOUNT.cmbBox.Add(cursor3.GetString("User"))
	Next
End Sub
Sub BUTTON_SECURITY_CONFIRM_Click
	If EDITTEXT_PASSWORD.Text = "" Then
		ToastMessageShow("Empty Password", False)
	Else
		cursor2 = connection.ExecQuery("SELECT * FROM users_table WHERE user = '"&CMB_ACCOUNT.cmbBox.SelectedItem&"' and pass = '"&EDITTEXT_PASSWORD.Text&"'")
		If cursor2.RowCount > 0 Then
			If security_trigger = "AUTO FILL" Then
				Msgbox2Async("Are you sure you want to AUTO FILL this picklist?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
					EDITTEXT_PASSWORD.Text = ""
					CTRL.HideKeyboard
					AUTO_FILL
				Else

				End If
			Else If security_trigger = "OVERWRITE" Then
				PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
				EDITTEXT_PASSWORD.Text = ""
				CTRL.HideKeyboard
				UPDATE_PREPARING
			Else If security_trigger = "AUTO OS" Then
				Msgbox2Async("Are you sure you want to AUTO OS this picklist?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
					EDITTEXT_PASSWORD.Text = ""
					CTRL.HideKeyboard
					AUTO_OS
				Else
			
				End If
			Else
				ToastMessageShow("Wrong Password",True)
			End If
		End If

	End If
End Sub
Sub BUTTON_SECURITY_CANCEL_Click
	PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
	EDITTEXT_PASSWORD.Text = ""
	CTRL.HideKeyboard
End Sub
Sub PANEL_BG_SECURITY_Click
	Return True
End Sub

Sub DELETE_PICKLIST_PREPARED
	PANEL_BG_TYPE.SetVisibleAnimated(300,False)
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
	LABEL_HEADER_TEXT.Text = "Uploading Picklist"
	LABEL_MSGBOX2.Text = "Data getting ready.."
	Dim cmd As DBCommand = CreateCommand("delete_picklist_prepared", Array(picklist_id.Trim))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_PICKLIST_PREPARED
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
	End If
	js.Release
End Sub
Sub INSERT_PICKLIST_PREPARED
	Sleep(0)
	cursor3 = connection.ExecQuery("SELECT * FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id.Trim&"' GROUP BY product_id, unit")
	If cursor3.RowCount > 0 Then
		For i3 = 0 To cursor3.RowCount - 1
			cursor3.Position = i3
			Dim cmd As DBCommand = CreateCommand("insert_picklist_prepared", Array As String(cursor3.GetString("picklist_id"), cursor3.GetString("picklist_name"), cursor3.GetString("picklist_date"), _
			cursor3.GetString("principal_id"),cursor3.GetString("principal_name"), cursor3.GetString("product_id"),cursor3.GetString("product_variant"),cursor3.GetString("product_description"), _
			cursor3.GetString("unit"), cursor3.GetString("quantity"),  cursor3.GetString("prepared_quantity"),cursor3.GetString("prepared_total_pcs"), cursor3.GetString("prepared_status"), _
			cursor3.GetString("prepared_by"), cursor3.GetString("prepared_date"),  cursor3.GetString("prepared_time"),cursor3.GetString("scan_code"), cursor3.GetString("reason"), _
			cursor3.GetString("picker"),"UNCONFIRMED"))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Uploading :" & cursor3.GetString("product_description")
			Else
				Log("INSERT_PICKLIST_PREPARED ERROR: " & js.ErrorMessage)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next
		CHECK_PICKLIST_PREPARED
	End If
End Sub
Sub CHECK_PICKLIST_PREPARED
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_count_picklist_prepared", Array(picklist_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				prepared_count = row(res.Columns.Get("prepared_count"))
			Next
		Else
			prepared_count = 0
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
	Sleep(100)
	cursor8 = connection.ExecQuery("SELECT count(product_id) as 'uploaded_count' FROM picklist_preparing_disc_table WHERE picklist_id = '"&picklist_id.Trim&"'")
	If cursor8.RowCount > 0 Then
		For ise = 0 To cursor8.RowCount - 1
			cursor8.Position = ise
			uploaded_count = cursor8.GetString("uploaded_count")
'			uploaded_count = 5
		Next
	Else
		uploaded_count = 0
	End If
	Sleep(0)
	LABEL_MSGBOX2.Text = "Total Served : " & prepared_count & " " & "Total Uploaded : " & uploaded_count
	Sleep(2000)
	If uploaded_count <> prepared_count Then
		jr.Release
		Msgbox2Async("Uploading Order Not Complete. Please reupload picklist", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		DELETE_PICKLIST_PREPARED_TRAIL
	End If
End Sub
Sub DELETE_PICKLIST_PREPARED_TRAIL
	Dim cmd As DBCommand = CreateCommand("delete_picklist_trail", Array(picklist_id.Trim,"PREPARING"))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_PICKLIST_PREPARED_TRAIL
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
	End If
	js.Release
End Sub
Sub INSERT_PICKLIST_PREPARED_TRAIL
	Sleep(0)
	cursor4 = connection.ExecQuery("SELECT * FROM picklist_table_trail WHERE picklist_id = '"&picklist_id.Trim&"'")
	If cursor4.RowCount > 0 Then
		For i3 = 0 To cursor4.RowCount - 1
			cursor4.Position = i3
			Dim cmd As DBCommand = CreateCommand("insert_picklist_trail", Array As String(cursor4.GetString("picklist_id"), cursor4.GetString("picklist_name"), cursor4.GetString("picklist_date"), _
			cursor4.GetString("product_id"),cursor4.GetString("product_variant"),cursor4.GetString("product_description"), _
			cursor4.GetString("unit"), cursor4.GetString("quantity"),  cursor4.GetString("reason"),cursor4.GetString("preparing_checker"),cursor4.GetString("loading_checker"), cursor4.GetString("picker"), _
			cursor4.GetString("transaction_type"), cursor4.GetString("date_time_registered"),  cursor4.GetString("tab_id")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Uploading :" & cursor4.GetString("product_description")
			Else
				Log("INSERT_PICKLIST_PREPARED_TRAIL ERROR: " & js.ErrorMessage)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next
	End If
	Sleep(0)
	UPDATE_PREPARED
End Sub
Sub UPDATE_PREPARED
	LABEL_MSGBOX2.Text = "Updating Status..."
	Dim cmd As DBCommand = CreateCommand("update_picklist_prepared", Array(DateTime.Date(DateTime.Now) &" "&DateTime.Time(DateTime.Now), picklist_id.Trim))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		LABEL_MSGBOX2.Text = "Picklist uploaded succesfully..."
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Msgbox2Async("Picklist prepared and uploaded successfully.", "Notice", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		CLEAR_PICKLIST
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("UPDATE_PREPARED ERROR: " & js.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	js.Release
End Sub
Sub BUTTON_PREPARE_Click
	Msgbox2Async("Are you sure you want to PREPARED this picklist?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		DELETE_PICKLIST_PREPARED
	Else
		
	End If
End Sub

Sub CLEAR_PICKLIST
	ProgressDialogShow2("Clearing Picklist",False)
	ACToolBarLight1.Menu.RemoveItem(1)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "Load Picklist", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Load Picklist", addBitmap)
	Sleep(1000)
	LABEL_LOAD_NAME.Text = "-"
	LABEL_LOAD_DATE.Text = "-"
	LABEL_LOAD_STATUS.Text = "-"
	LABEL_LOAD_ONHOLD.Text = "-"
	BUTTON_PICKER.Enabled = False
	BUTTON_AUTOFILL.Enabled = False
	BUTTON_OS.Enabled = False
	BUTTON_PREPARE.Enabled = False
	picklist_id = ""
	PREPARING_TABLE.Clear
	ProgressDialogHide()
End Sub
Sub ENABLE_PICKLIST
	ACToolBarLight1.Menu.RemoveItem(1)
	Sleep(0)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "Clear", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Clear", clearBitmap)
	BUTTON_PICKER.Enabled = True
	BUTTON_AUTOFILL.Enabled = True
	BUTTON_OS.Enabled = True
End Sub

Sub LOAD_REASON
	CMB_REASON.cmbBox.Clear
	CMB_REASON.cmbBox.Add("WRONG SIZE")
	CMB_REASON.cmbBox.Add("WRONG FLAVOR/COLOR")
	CMB_REASON.cmbBox.Add("WRONG SKU")
	CMB_REASON.cmbBox.Add("WRONG SCAN")
End Sub
Sub GET_UNIT	
	cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESC_PREPARING.Text&"'")
	For qrow = 0 To cursor3.RowCount - 1
		cursor3.Position = qrow	
		product_id = cursor3.GetString("product_id")
		LABEL_LOAD_VARIANT_PREPARING.Text = cursor3.GetString("product_variant")
		
		CMB_UNIT.cmbBox.Clear
		If cursor3.GetString("CASE_UNIT_PER_PCS") > 0 Then
			CMB_UNIT.cmbBox.Add("CASE")
		End If
		If cursor3.GetString("PCS_UNIT_PER_PCS") > 0 Then
			CMB_UNIT.cmbBox.Add("PCS")
		End If
		If cursor3.GetString("DOZ_UNIT_PER_PCS") > 0 Then
			CMB_UNIT.cmbBox.Add("DOZ")
		End If
		If cursor3.GetString("BOX_UNIT_PER_PCS") > 0 Then
			CMB_UNIT.cmbBox.Add("BOX")
		End If
		If cursor3.GetString("BAG_UNIT_PER_PCS") > 0 Then
			CMB_UNIT.cmbBox.Add("BAG")
		End If
		If cursor3.GetString("PACK_UNIT_PER_PCS") > 0 Then
			CMB_UNIT.cmbBox.Add("PACK")
		End If
	Next
End Sub
Sub CMB_PICKER_SelectedIndexChanged (Index As Int)
	CMB_REASON.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_REASON.cmbBox)
End Sub
Sub CMB_REASON_SelectedIndexChanged (Index As Int)
	CMB_UNIT.SelectedIndex = 1
	Sleep(0)
	OpenSpinner(CMB_UNIT.cmbBox)
End Sub
Sub CMB_UNIT_SelectedIndexChanged (Index As Int)
	EDITTEXT_QUANTITY.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
End Sub
Sub BUTTON_OKAY_Click
	If EDITTEXT_QUANTITY.Text = "0" Or  EDITTEXT_QUANTITY.Text = "" Then
		ToastMessageShow("Quantity is empty", False)
	Else	
			If CMB_PICKER.cmbBox.SelectedIndex = -1 Then CMB_PICKER.cmbBox.SelectedIndex = 0
			If CMB_REASON.cmbBox.SelectedIndex = -1 Then CMB_REASON.cmbBox.SelectedIndex = 0
			If CMB_UNIT.cmbBox.SelectedIndex = -1 Then CMB_UNIT.cmbBox.SelectedIndex = 0
		
			Dim query As String = "INSERT INTO picklist_table_trail VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text _
			,product_id,LABEL_LOAD_VARIANT_PREPARING.Text,LABEL_LOAD_DESC_PREPARING.Text,CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QUANTITY.Text _
			,CMB_REASON.cmbBox.SelectedItem,LOGIN_MODULE.username,"-",CMB_PICKER.cmbBox.SelectedItem,DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now),"PREPARING",LOGIN_MODULE.tab_id))
			Sleep(0)
		
			PANEL_BG_TRAIL.SetVisibleAnimated(300, False)
			ProgressDialogHide
	End If
End Sub

Sub BUTTON_OS_Click
	GET_SECURITY
	Sleep(0)
	PANEL_BG_SECURITY.SetVisibleAnimated(300, True)
	PANEL_BG_SECURITY.BringToFront
	EDITTEXT_PASSWORD.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_PASSWORD)
	security_trigger = "AUTO OS"
End Sub

Sub AUTO_OS
	cursor1 = connection.ExecQuery("SELECT * FROM picklist_preparing_ref_table WHERE picklist_id = '"&picklist_id.trim&"' ORDER BY product_variant ASC, product_description ASC")
	If cursor1.RowCount > 0 Then
		ProgressDialogShow2("Loading...",False)
		For arow = 0 To cursor1.RowCount - 1
			cursor1.Position = arow
			Sleep(0)
			cursor2 = connection.ExecQuery("SELECT * FROM picklist_preparing_disc_table WHERE product_id = '"&cursor1.GetString("product_id")&"' and unit = '"&cursor1.GetString("unit")&"' and picklist_id = '"&picklist_id.trim&"'")
			If cursor2.RowCount > 0 Then
				For xrow = 0 To cursor2.RowCount - 1
					cursor2.Position = xrow
				Next
			Else
				LABEL_LOAD_DESC_PREPARING.text = cursor1.GetString("product_description")
				GET_PRODUCT_DETAILS
				Sleep(0)
				Dim query As String = "INSERT INTO picklist_preparing_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				connection.ExecNonQuery2(query,Array As String(picklist_id.trim,LABEL_LOAD_NAME.Text, LABEL_LOAD_DATE.Text,principal_id,principal_name, cursor1.GetString("product_id"), _
				cursor1.GetString("product_variant"), cursor1.GetString("product_description"),cursor1.GetString("unit"), cursor1.GetString("quantity"), 0, _
				0, "ON HOLD",LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Date(DateTime.Now),"AUTO OS " & CMB_ACCOUNT.cmbBox.SelectedItem, _
				"OUT OF STOCK (AUTO OS)",LOGIN_MODULE.tab_id,0,picker))
			End If
		Next
		
		
		ToastMessageShow("AUTO OS SUCCESSFUL",False)
		ProgressDialogHide
	End If
End Sub