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
	
	Dim security_trigger As String
	Dim over_short_trigger As String
	
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
	
	Dim scan_next_trigger As String
	Dim scan_trigger As String
	
	Dim loading_count As String
	Dim downloaded_count As String
	Dim loaded_count As Int
	Dim uploaded_count As Int
	
	Dim unit_trigger As String
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
	Private LOADING_TABLE As B4XTable
	
	Private PANEL_BG_TYPE As Panel
	Private EDITTEXT_TYPE As EditText

	Private LABEL_LOAD_NAME As Label
	Private LABEL_LOAD_DATE As Label
	Private LABEL_LOAD_STATUS As Label
	Private LABEL_LOAD_ONHOLD As Label
	Private BUTTON_DELIVERY As Button
	Private BUTTON_AUTOFILL As Button
	Private BUTTON_UPLOAD As Button
	
	Private LABEL_HEADER_TEXT As Label
	Private LABEL_MSGBOX1 As Label
	Private LABEL_MSGBOX2 As Label
	Private PANEL_BG_MSGBOX As Panel
	
	Private PANEL_BG_SECURITY As Panel
	Private CMB_ACCOUNT As B4XComboBox
	Private EDITTEXT_PASSWORD As EditText
	
	Private LABEL_LOAD_NAME As Label
	Private LABEL_LOAD_DATE As Label
	Private EDITTEXT_PCS_QTY_LOADING As EditText
	Private EDITTEXT_CASE_QTY_LOADING As EditText
	Private EDITTEXT_BOX_QTY_LOADING As EditText
	Private EDITTEXT_DOZ_QTY_LOADING As EditText
	Private EDITTEXT_PACK_QTY_LOADING As EditText
	Private EDITTEXT_BAG_QTY_LOADING As EditText
	Private LABEL_LOAD_VARIANT_LOADING As Label
	Private LABEL_LOAD_DESC_LOADING As Label
	Private LABEL_PCS_QTY_LOADING As Label
	Private LABEL_CASE_QTY_LOADING As Label
	Private LABEL_BOX_QTY_LOADING As Label
	Private LABEL_DOZ_QTY_LOADING As Label
	Private LABEL_PACK_QTY_LOADING As Label
	Private LABEL_BAG_QTY_LOADING As Label
	Private PANEL_BG_LOADING As Panel
	Private BUTTON_PCS_LOADING As Button
	Private BUTTON_CASE_LOADING As Button
	Private BUTTON_BOX_LOADING As Button
	Private BUTTON_PACK_LOADING As Button
	Private BUTTON_DOZ_LOADING As Button
	Private BUTTON_BAG_LOADING As Button
	Private LABEL_LOAD_CHECKER As Label
	Private PANEL_BG_TRAIL As Panel
	Private CMB_PICKER As B4XComboBox
	Private CMB_REASON As B4XComboBox
	Private EDITTEXT_QUANTITY As EditText
	Private CMB_UNIT As B4XComboBox
	Private EDITTEXT_PUSHCART As EditText
	Private EDITTEXT_HELPER3 As EditText
	Private EDITTEXT_HELPER2 As EditText
	Private EDITTEXT_HELPER1 As EditText
	Private EDITTEXT_DRIVER As EditText
	Private EDITTEXT_PLATE As EditText
	Private PANEL_BG_DELIVERY As Panel
	Private CMB_SUPERVISOR As B4XComboBox
	Private LABEL_LOAD_SCANCODE As Label
	Private EDITTEXT_TRAIL_PASS As EditText
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
	Activity.LoadLayout("picklist_loading")
	
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
	
	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", False)
	End If

	Dim bg As ColorDrawable
	bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_TYPE.Background = bg
	EDITTEXT_PASSWORD.Background = bg
	
	Dim bg2 As ColorDrawable
	bg2.Initialize2(Colors.RGB(182,217,255), 0, 1, Colors.White)
	EDITTEXT_PCS_QTY_LOADING.Background = bg2
	EDITTEXT_CASE_QTY_LOADING.Background = bg2
	EDITTEXT_DOZ_QTY_LOADING.Background = bg2
	EDITTEXT_BOX_QTY_LOADING.Background = bg2
	EDITTEXT_BAG_QTY_LOADING.Background = bg2
	EDITTEXT_PACK_QTY_LOADING.Background = bg2
	
	Dim bg3 As ColorDrawable
	bg3.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_QUANTITY.Background = bg3
	
	
	Dim bg4 As ColorDrawable
	bg4.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_PLATE.Background = bg4
	
	Dim bg5 As ColorDrawable
	bg5.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_PUSHCART.Background = bg5
	
	Dim bg6 As ColorDrawable
	bg6.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_DRIVER.Background = bg6
	EDITTEXT_HELPER1.Background = bg6
	EDITTEXT_HELPER2.Background = bg6
	EDITTEXT_HELPER3.Background = bg6
'	phone.SetScreenOrientation(0)

	Dim bg7 As ColorDrawable
	bg7.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_TRAIL_PASS.Background = bg7
	
	Sleep(0)
	LOAD_LOADING_HEADER
	
	Sleep(0)
	Dim Ref As Reflector
	Ref.Target = EDITTEXT_TYPE ' The text field being referenced
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
	picklist_id = ""
	
	Sleep(0)
	
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"

	BG_ENABLE.Initialize2(Colors.RGB(27,255,0),0,1,Colors.White)
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

#Region BLUETOOTH
Sub ShowPairedDevices
	Dim mac As String
	Dim PairedDevices As Map
	PairedDevices = serial1.GetPairedDevices
	Dim ls As List
	ls.Initialize
	For Iq = 0 To PairedDevices.Size - 1
		mac = PairedDevices.Get(PairedDevices.GetKeyAt(Iq))
		ls.add("Scanner " & mac.SubString2 (mac.Length - 4, mac.Length ))
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
			ScannerMacAddress=PairedDevices.Get(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get(Result))))
			'convert the name to mac address and connect
			Log(PairedDevices.GetKeyAt(ls.IndexOf(ls.Get (Result))))
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
	If scan_trigger = 0 Then
		Log("Received: " & BytesToString(Buffer, 0, Buffer.Length, "UTF8"))
		If picklist_id = "" Then
			picklist_id = BytesToString(Buffer, 0, Buffer.Length, "UTF8").Trim
			VALIDATE_PICKLIST_STATUS
		Else
#Region Scan Barcode
			Log("aaa")
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
				scan_next_trigger = 1
				CTRL.HideKeyboard
				Dim trigger As Int = 0
			
				cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") &"' as INTEGER) or case_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or box_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or bag_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or pack_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER)) and prod_status = '0' ORDER BY product_id")
				If cursor2.RowCount >= 2 Then
					Dim ls As List
					ls.Initialize
					For row = 0 To cursor2.RowCount - 1
						cursor2.Position = row
						ls.Add(cursor2.GetString("product_desc"))
						DateTime.DateFormat = "yyyy-MM-dd"
					Next
					InputListAsync(ls, "Choose Description :", -1, True) 'show list with paired devices
					Wait For InputList_Result (Result As Int)
					If Result <> DialogResponse.CANCEL Then
						LABEL_LOAD_DESC_LOADING.Text = ls.Get(Result)
						trigger = 0
					Else
						trigger = 1
					End If
				
				
					'SINGLE SKU
				
				Else if cursor2.RowCount = 1 Then
					For row = 0 To cursor2.RowCount - 1
						cursor2.Position = row
						Log(1)
						LABEL_LOAD_DESC_LOADING.Text = cursor2.GetString("product_desc")
						trigger = 0
					Next
				
				
				Else
					Msgbox2Async("The barcode you scanned is not REGISTERED IN THE SYSTEM.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					trigger = 1
					CLEAR_PICKLIST_SKU
					Sleep(0)
					PANEL_BG_LOADING.SetVisibleAnimated(300, False)
'		CLEAR_INPUT
				End If
				Sleep(0)
				If trigger = 0 Then
					scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8").Trim
					Sleep(0)
					ProgressDialogShow2("Loading...",False)
					CLEAR_PICKLIST_SKU
					Sleep(0)
					GET_PRODUCT_DETAILS
					Sleep(0)
			
				
					cursor4 = connection.ExecQuery("SELECT * FROM picklist_loading_ref_table WHERE product_description ='"&LABEL_LOAD_DESC_LOADING.Text&"' AND picklist_id = '"&picklist_id&"'")
					If cursor4.RowCount > 0 Then
						For row = 0 To cursor4.RowCount - 1
							cursor4.Position = row
							LABEL_LOAD_VARIANT_LOADING.Text = cursor4.GetString("product_variant")
							If cursor4.GetString("unit") = "CASE" Then
								LABEL_CASE_QTY_LOADING.Text = cursor4.GetString("quantity")
								EDITTEXT_CASE_QTY_LOADING.Text = cursor4.GetString("quantity")
								BUTTON_CASE_LOADING.Enabled = True
								BUTTON_CASE_LOADING.Background = BG_ENABLE
							End If
							Sleep(0)
							If cursor4.GetString("unit") = "PCS" Then
								LABEL_PCS_QTY_LOADING.Text = cursor4.GetString("quantity")
								EDITTEXT_PCS_QTY_LOADING.Text = cursor4.GetString("quantity")
								BUTTON_PCS_LOADING.Enabled = True
								BUTTON_PCS_LOADING.Background = BG_ENABLE
							End If
							Sleep(0)
							If cursor4.GetString("unit") = "BOX" Then
								LABEL_BOX_QTY_LOADING.Text = cursor4.GetString("quantity")
								EDITTEXT_BOX_QTY_LOADING.Text = cursor4.GetString("quantity")
								BUTTON_BOX_LOADING.Enabled = True
								BUTTON_BOX_LOADING.Background = BG_ENABLE
							End If
							Sleep(0)
							If cursor4.GetString("unit") = "DOZ" Then
								LABEL_DOZ_QTY_LOADING.Text = cursor4.GetString("quantity")
								EDITTEXT_DOZ_QTY_LOADING.Text = cursor4.GetString("quantity")
								BUTTON_DOZ_LOADING.Enabled = True
								BUTTON_DOZ_LOADING.Background = BG_ENABLE
							End If
							Sleep(0)
							If cursor4.GetString("unit") = "BAG" Then
								LABEL_BAG_QTY_LOADING.Text = cursor4.GetString("quantity")
								EDITTEXT_BAG_QTY_LOADING.Text = cursor4.GetString("quantity")
								BUTTON_BAG_LOADING.Enabled = True
								BUTTON_BAG_LOADING.Background = BG_ENABLE
							End If
							If cursor4.GetString("unit") = "PACK" Then
								LABEL_PACK_QTY_LOADING.Text = cursor4.GetString("quantity")
								EDITTEXT_PACK_QTY_LOADING.Text = cursor4.GetString("quantity")
								BUTTON_PACK_LOADING.Enabled = True
								BUTTON_PACK_LOADING.Background = BG_ENABLE
							End If
						Next
						NOT_UNIT_TRIGGER
						GET_TOTAL_SERVED
						Sleep(0)
						PANEL_BG_LOADING.SetVisibleAnimated(300,True)
						PANEL_BG_LOADING.BringToFront
						reason = "N/A"
						Sleep(0)
						ORDER_SPEECH
						ProgressDialogHide()
					Else
						PANEL_BG_LOADING.SetVisibleAnimated(300, False)
						Msgbox2Async("The product you scanned :"& CRLF &""&LABEL_LOAD_DESC_LOADING.Text&" "& CRLF &"IS NOT THE PICKLIST.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						Wait For Msgbox_Result (Result As Int)
						PANEL_BG_TRAIL.SetVisibleAnimated(300,True)
						GET_VISOR
						Sleep(0)
						LOAD_REASON
						GET_UNIT
						CMB_PICKER.SelectedIndex = -1
						Sleep(0)
						OpenSpinner(CMB_PICKER.cmbBox)
						EDITTEXT_QUANTITY.Enabled = True
						over_short_trigger = 0
						scan_trigger = 1
						ProgressDialogHide
					End If
				End If
				Sleep(0)
			End If
#end Region
		End If
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
	LABEL_MSGBOX2.Text = "Getting Prepared..."
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_picklist_confirmed", Array(picklist_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		connection.ExecNonQuery("DELETE FROM picklist_loading_ref_table WHERE picklist_id = '"&picklist_id&"'")
		Sleep(0)
		connection.ExecNonQuery("UPDATE picklist_loading_disc_table SET confirm_status = '1' WHERE picklist_id = '"&picklist_id&"'")
		Sleep(0)
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				LABEL_LOAD_NAME.Text = row(res.Columns.Get("picklist_name"))
				LABEL_LOAD_DATE.Text = row(res.Columns.Get("picklist_date"))
				
				connection.ExecNonQuery("INSERT INTO picklist_loading_ref_table VALUES ('"&row(res.Columns.Get("picklist_id"))&"','"&row(res.Columns.Get("picklist_name"))&"','"&row(res.Columns.Get("picklist_date"))& _
				"','"&row(res.Columns.Get("product_id"))&"','"&row(res.Columns.Get("product_variant"))&"','"&row(res.Columns.Get("product_description"))& _
				"','"&row(res.Columns.Get("unit"))&"','"&row(res.Columns.Get("prepared_quantity"))&"','"&row(res.Columns.Get("prepared_total_pcs"))&"','"&DateTime.Date(DateTime.Now) &" " &DateTime.Time(DateTime.Now)&"','"&LOGIN_MODULE.tab_id&"','"&LOGIN_MODULE.username&"')")
				Sleep(50)
				connection.ExecNonQuery("UPDATE picklist_loading_disc_table SET confirm_status = '0' WHERE picklist_id = '"&row(res.Columns.Get("picklist_id"))&"' and product_id = '"&row(res.Columns.Get("product_id"))&"' and unit = '"&row(res.Columns.Get("unit"))&"' and quantity = '"&row(res.Columns.Get("quantity"))&"'")
				LABEL_MSGBOX2.Text = "Getting Order : " & row(res.Columns.Get("product_description"))
				
			Next
			CHECK_PICKLIST_LOADING
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
Sub CHECK_PICKLIST_LOADING
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_count_picklist_confirmed", Array(picklist_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				loading_count = row(res.Columns.Get("loading_count"))
			Next
		Else
			loading_count = 0
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
	Sleep(100)
	cursor8 = connection.ExecQuery("SELECT count(product_id) as 'downloaded_count' FROM picklist_loading_ref_table WHERE picklist_id = '"&picklist_id.Trim&"'")
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
	LABEL_MSGBOX2.Text = "Total Order : " & loading_count & " " & "Total Downloaded : " & downloaded_count
	Sleep(2000)
	If downloaded_count <> loading_count Then
		ToastMessageShow("Downloading Order Not Complete. Redownloading", False)
		Sleep(0)
		DOWNLOAD_PICKLIST
	Else
		Dim msg As String = "This are the sku that will be deleted or change quantity:" & CRLF
			
			
		cursor5 = connection.ExecQuery("SELECT * FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id&"' and confirm_status = '1'")
		If cursor5.RowCount > 0 Then
			For ise = 0 To cursor5.RowCount - 1
				cursor5.Position = ise
				msg = msg & " " & cursor5.GetString("product_description") & " / " & cursor5.GetString("quantity") & "-" & cursor5.GetString("unit") & CRLF
			Next
				
				
			Msgbox2Async(msg, "PICKLIST ADJUSTMENT", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
		connection.ExecNonQuery("DELETE FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id&"' and confirm_status = '1'")
			
		LABEL_MSGBOX2.Text = "Picklist Downloaded Successfully.."
		Sleep(0)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		LOAD_LOADING_HEADER
		Sleep(0)
		LOAD_LOADING_PICKLIST
		CTRL.HideKeyboard
		Sleep(0)
		GET_PREPARED_DETAILS
		Sleep(0)
		GET_DELIVERY_DETAILS
		Sleep(0)
		OPEN_DELIVERY
		scan_next_trigger = 0
		scan_trigger = 0
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
				If row(res.Columns.Get("PickListStatus")) = "CONFIRMED" Then
					If row(res.Columns.Get("PreparingChecker")) = LOGIN_MODULE.username Then
						Msgbox2Async("You are the Preparing Checker of this picklist, are you authorize to Load this?", _
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
					Else
						UPDATE_LOADING
					End If
				Else if row(res.Columns.Get("PickListStatus")) = "LOADING" Then
					Log(98)
					If row(res.Columns.Get("LoadingTabletID")) = LOGIN_MODULE.tab_id Then
						DOWNLOAD_PICKLIST
					Else
						Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")) & CRLF & _
						" is NOW LOADING by :" & CRLF & _
						" Loading Checker : " & row(res.Columns.Get("LoadingChecker")) & CRLF & _
						" Date & Time Loading : " & row(res.Columns.Get("LoadingDateTIme")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("LoadingTabletID")) & CRLF & _
						" Do you want to overwrite this picklist?", _
			   			"Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						Wait For Msgbox_Result (Result As Int)
						If Result = DialogResponse.POSITIVE Then
							UPDATE_LOADING
						Else
		
						End If
					End If
				Else if row(res.Columns.Get("PickListStatus")) = "LOADED" Then
					If row(res.Columns.Get("LoadingTabletID")) = LOGIN_MODULE.tab_id And row(res.Columns.Get("LoadingChecker")) = LOGIN_MODULE.username Then
						Msgbox2Async("You've already prepared this picklist, Do you want to preparing check it again?", _
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
						
	
					Else
						Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")) & CRLF & _
						" is already Loaded by :" & CRLF & _
						" Preparing Checker : " & row(res.Columns.Get("LoadingChecker")) & CRLF & _
						" Date & Time Loading : " & row(res.Columns.Get("LoadingDateTIme")) & CRLF & _
						" Date & Time Loaded : " & row(res.Columns.Get("LoadingDateTime")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("LoadingTabletid")), _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
					End If
				Else
					Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")) & CRLF & _
						" is in status of :" & row(res.Columns.Get("PickListStatus")) & " cannot be load.", _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				End If
			Next
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			Msgbox2Async("The Picklist ID you type/scan is not existing in the system. Please double check your Picklist ID.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Sleep(0)
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
Sub UPDATE_LOADING
	LABEL_MSGBOX2.Text = "Updating Status..."
	Dim cmd As DBCommand = CreateCommand("update_picklist_loading", Array(LOGIN_MODULE.tab_id,LOGIN_MODULE.username,DateTime.Date(DateTime.Now) &" "&DateTime.Time(DateTime.Now), picklist_id.Trim))
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
	BUTTON_DELIVERY.Enabled = False
	BUTTON_AUTOFILL.Enabled = False
	BUTTON_UPLOAD.Enabled = False
	picklist_id = ""
	LOADING_TABLE.Clear
	ProgressDialogHide()
End Sub
Sub ENABLE_PICKLIST
	ACToolBarLight1.Menu.RemoveItem(1)
	Sleep(0)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "Clear", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Clear", clearBitmap)
	BUTTON_DELIVERY.Enabled = True
	BUTTON_AUTOFILL.Enabled = True
End Sub

Sub BUTTON_CANCEL_Click
	PANEL_BG_TYPE.SetVisibleAnimated(300,False)
End Sub
Sub BUTTON_LOAD_Click
	picklist_id = EDITTEXT_TYPE.Text.ToUpperCase
	Sleep(0)
	VALIDATE_PICKLIST_STATUS
	Sleep(0)
	ENABLE_PICKLIST
End Sub

Sub LOAD_LOADING_HEADER
	NameColumn(0)=LOADING_TABLE.AddColumn("Status", LOADING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(1)=LOADING_TABLE.AddColumn("Product Variant", LOADING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(2)=LOADING_TABLE.AddColumn("Product Description", LOADING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(3)=LOADING_TABLE.AddColumn("Unit", LOADING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(4)=LOADING_TABLE.AddColumn("Served", LOADING_TABLE.COLUMN_TYPE_TEXT)
	NameColumn(5)=LOADING_TABLE.AddColumn("Loaded", LOADING_TABLE.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_LOADING_PICKLIST
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	
	cursor4 = connection.ExecQuery("SELECT a.*, b.loaded_status , b.loaded_quantity FROM picklist_loading_ref_table as a LEFT OUTER JOIN picklist_loading_disc_table as b ON " & _
	"a.picklist_id = b.picklist_id And a.product_id = b.product_id And a.unit = b.unit " & _
	"WHERE a.picklist_id = '"&picklist_id&"' " & _
	"ORDER BY a.product_variant ASC")
	If cursor4.RowCount > 0 Then
		For ia = 0 To cursor4.RowCount - 1
			Sleep(10)
			cursor4.Position = ia
			If cursor4.GetString("loaded_status") = Null Or cursor4.GetString("loaded_status") = "" Then
				Sleep(10)
				Dim row(6) As Object
				row(0) = "UNLOADED"
				row(1) = cursor4.GetString("product_variant")
				row(2) = cursor4.GetString("product_description")
				row(3) = cursor4.GetString("unit")
				row(4) = cursor4.GetString("quantity")
				row(5) = 0
				Data.Add(row)
			Else
				Dim row(6) As Object
				row(0) = cursor4.GetString("loaded_status")
				row(1) = cursor4.GetString("product_variant")
				row(2) = cursor4.GetString("product_description")
				row(3) = cursor4.GetString("unit")
				row(4) = cursor4.GetString("quantity")
				row(5) = cursor4.GetString("loaded_quantity")
				Data.Add(row)
			End If
		Next
		
		
		
		LOADING_TABLE.NumberOfFrozenColumns = 1
		LOADING_TABLE.RowHeight = 50dip
		Sleep(100)
		GET_STATUS
		Sleep(0)
		ProgressDialogHide
	Else
		
		
		ToastMessageShow("Picklist is empty", False)
	End If
	LOADING_TABLE.SetData(Data)
	
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(LOADING_TABLE)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub LOADING_TABLE_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(2), NameColumn(3),NameColumn(4), NameColumn(5))

		Dim MaxWidth As Int
		Dim MaxHeight As Int
		For i = 0 To LOADING_TABLE.VisibleRowIds.Size
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

		For i = 0 To LOADING_TABLE.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			lbl.Font = xui.CreateDefaultBoldFont(18)
		Next
	Next
	
	For Each Column As B4XTableColumn In Array (NameColumn(0))
		Column.InternalSortMode= "DESC"
	Next
	
	For i = 0 To LOADING_TABLE.VisibleRowIds.Size - 1
		Dim RowId As Long = LOADING_TABLE.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(0).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = LOADING_TABLE.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(0).Id)
			If OtherColumnValue = ("UNLOADED") Then
				clr = xui.Color_Red
			Else If OtherColumnValue = ("LOADED") Then
				clr = xui.Color_Green
			Else
				clr = xui.Color_Yellow
			End If
			pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Colors.RGB(215,215,215), 0)
			
		End If
	Next
'	For Each Column As B4XTableColumn In Array (NameColumn(0))
'		Column.InternalSortMode= "ASC"
'	Next
	If ShouldRefresh Then
		LOADING_TABLE.Refresh
		XSelections.Clear
	End If
End Sub
Sub LOADING_TABLE_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = LOADING_TABLE.GetRow(RowId)
	Dim ls As List
	ls.Initialize
	ls.Add("BARCODE NOT REGISTERED IN THE SYSTEM")
	ls.Add("NO ACTUAL BARCODE")
	ls.Add("NO SCANNER")
	ls.Add("SCANNER CAN'T READ BARCODE")
	ls.Add("DAMAGE BARCODE")
	ls.Add("FREE ITEM")
	InputListAsync(ls, "Choose reason", -1, True) 'show list with paired devices
	Wait For InputList_Result (Result2 As Int)
	If Result2 <> DialogResponse.CANCEL Then
		ProgressDialogShow2("Loading...",False)
		reason = ls.Get(Result2)
		scan_code = "N/A"
		Sleep(0)
		LABEL_LOAD_DESC_LOADING.Text = RowData.Get("Product Description")
		CLEAR_PICKLIST_SKU
		Sleep(0)
		GET_PRODUCT_DETAILS
		Sleep(0)
		cursor4 = connection.ExecQuery("SELECT * FROM picklist_loading_ref_table WHERE product_description ='"&LABEL_LOAD_DESC_LOADING.Text&"' AND picklist_id = '"&picklist_id&"'")
		If cursor4.RowCount > 0 Then
			For arow = 0 To cursor4.RowCount - 1
				cursor4.Position = arow
				LABEL_LOAD_VARIANT_LOADING.Text = cursor4.GetString("product_variant")
				If cursor4.GetString("unit") = "CASE" Then
					LABEL_CASE_QTY_LOADING.Text = cursor4.GetString("quantity")
					EDITTEXT_CASE_QTY_LOADING.Text = cursor4.GetString("quantity")
					BUTTON_CASE_LOADING.Enabled = True
					BUTTON_CASE_LOADING.Background = BG_ENABLE
				End If
				Sleep(0)
				If cursor4.GetString("unit") = "PCS" Then
					LABEL_PCS_QTY_LOADING.Text = cursor4.GetString("quantity")
					EDITTEXT_PCS_QTY_LOADING.Text = cursor4.GetString("quantity")
					BUTTON_PCS_LOADING.Enabled = True
					BUTTON_PCS_LOADING.Background = BG_ENABLE
				End If
				Sleep(0)
				If cursor4.GetString("unit") = "BOX" Then
					LABEL_BOX_QTY_LOADING.Text = cursor4.GetString("quantity")
					EDITTEXT_BOX_QTY_LOADING.Text = cursor4.GetString("quantity")
					BUTTON_BOX_LOADING.Enabled = True
					BUTTON_BOX_LOADING.Background = BG_ENABLE
				End If
				Sleep(0)
				If cursor4.GetString("unit") = "DOZ" Then
					LABEL_DOZ_QTY_LOADING.Text = cursor4.GetString("quantity")
					EDITTEXT_DOZ_QTY_LOADING.Text = cursor4.GetString("quantity")
					BUTTON_DOZ_LOADING.Enabled = True
					BUTTON_DOZ_LOADING.Background = BG_ENABLE
				End If
				Sleep(0)
				If cursor4.GetString("unit") = "PACK" Then
					LABEL_PACK_QTY_LOADING.Text = cursor4.GetString("quantity")
					EDITTEXT_PACK_QTY_LOADING.Text = cursor4.GetString("quantity")
					BUTTON_PACK_LOADING.Enabled = True
					BUTTON_PACK_LOADING.Background = BG_ENABLE
				End If
				Sleep(0)
				If cursor4.GetString("unit") = "BAG" Then
					LABEL_BAG_QTY_LOADING.Text = cursor4.GetString("quantity")
					EDITTEXT_BAG_QTY_LOADING.Text = cursor4.GetString("quantity")
					BUTTON_BAG_LOADING.Enabled = True
					BUTTON_BAG_LOADING.Background = BG_ENABLE
				End If
				Sleep(0)
			Next
			NOT_UNIT_TRIGGER
			GET_TOTAL_SERVED
			Sleep(0)
			PANEL_BG_LOADING.SetVisibleAnimated(300,True)
			PANEL_BG_LOADING.BringToFront
			Sleep(0)
			ORDER_SPEECH
			ProgressDialogHide()
		Else
			Msgbox2Async("The product you scanned :"& CRLF &""&LABEL_LOAD_DESC_LOADING.Text&" "& CRLF &"IS NOT THE PICKLIST.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			CLEAR_PICKLIST_SKU
			Sleep(0)
			PANEL_BG_LOADING.SetVisibleAnimated(300, False)
		End If

	Else
		
	End If
	
End Sub
Sub LOADING_TABLE_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = LOADING_TABLE.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)

End Sub

Sub GET_STATUS
	Dim order_count As Int
	Dim serve_count As Int
	Dim on_hold_count As Int
	
	
	cursor4 = connection.ExecQuery("SELECT COUNT(product_id) as 'order_count' FROM picklist_loading_ref_table WHERE picklist_id = '"&picklist_id&"' GROUP BY product_id, unit")
	If cursor4.RowCount > 0 Then
		For ia = 0 To cursor4.RowCount - 1
			Sleep(0)
			cursor4.Position = ia
			order_count = order_count + 1 
		Next
		
		
	Else
		
		
		order_count = 0
	End If
	
	
	cursor5 = connection.ExecQuery("SELECT COUNT(product_id) as 'serve_count' FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id&"' GROUP BY product_id, unit")
	If cursor5.RowCount > 0 Then
		For ib = 0 To cursor5.RowCount - 1
			cursor5.Position = ib
			serve_count = serve_count + 1
		Next
		
		
	Else
		
		
		serve_count = 0
	End If

	LABEL_LOAD_STATUS.Text = serve_count & " / " & order_count
	If serve_count = order_count Then
		LABEL_LOAD_STATUS.Color = Colors.Green
		BUTTON_UPLOAD.Enabled = True
	Else
		LABEL_LOAD_STATUS.Color = Colors.Red
		BUTTON_UPLOAD.Enabled = False
	End If
		
	
	cursor6 = connection.ExecQuery("SELECT COUNT(product_id) as 'on_hold' FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id&"' AND loaded_status = 'ON HOLD' GROUP BY product_id, unit")
	If cursor6.RowCount > 0 Then
		For ib = 0 To cursor6.RowCount - 1
			cursor6.Position = ib
			on_hold_count = on_hold_count + 1
'			LABEL_LOAD_STATUS.Color = Colors.Red
'			BUTTON_UPLOAD.Enabled = False
		Next
		
		
	Else
		
		
		on_hold_count = 0
	End If
	
	If on_hold_count <> 0 Then
		LABEL_LOAD_STATUS.Color = Colors.Red
		BUTTON_UPLOAD.Enabled = False
	End If
	
	LABEL_LOAD_ONHOLD.Text = on_hold_count
	
	
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
					AUTO_FILL
				Else
		
				End If
			Else If security_trigger = "OVERWRITE" Then
				PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
				EDITTEXT_PASSWORD.Text = ""
				CTRL.HideKeyboard
				UPDATE_LOADING
			End If
		Else
			ToastMessageShow("Wrong Password", False)
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
	
	cursor4 = connection.ExecQuery("SELECT * FROM picklist_loading_ref_table WHERE picklist_id = '"&picklist_id&"'")
	If cursor4.RowCount > 0 Then
		PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
		EDITTEXT_PASSWORD.Text = ""
		CTRL.HideKeyboard
		Sleep(0)
		connection.ExecNonQuery("DELETE FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id.Trim&"' and quantity = '0'")
		ProgressDialogShow2("Auto Filling...", False)
		For ia = 0 To cursor4.RowCount - 1
			Sleep(0)
			cursor4.Position = ia
			
			cursor5 = connection.ExecQuery("SELECT * FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id&"' AND product_description = '"&cursor4.GetString("product_description")&"' AND unit = '"&cursor4.GetString("unit")&"'")
			If cursor5.RowCount > 0 Then
				
				
			Else
				
				
				LABEL_LOAD_DESC_LOADING.text = cursor4.GetString("product_description")
				GET_PRODUCT_DETAILS
				Sleep(0)
				
				Dim query As String = "INSERT INTO picklist_loading_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
				,cursor4.GetString("product_id"),cursor4.GetString("product_variant"),cursor4.GetString("product_description"),cursor4.GetString("unit"),cursor4.GetString("quantity"),cursor4.GetString("quantity"),cursor4.GetString("total_pieces") _
				,"LOADED",LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),"AUTO FILL " & CMB_ACCOUNT.cmbBox.SelectedItem,"AUTO FILL " & CMB_ACCOUNT.cmbBox.SelectedItem,LOGIN_MODULE.tab_id,0))
				
				
			End If
		Next
		
		
		ToastMessageShow("Auto Filling Succesfull", False)
		Sleep(0)
		LOAD_LOADING_PICKLIST
		Sleep(1000)
		ProgressDialogHide()
	Else
		
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
Sub ORDER_SPEECH
	Dim LOADING_SPEECH_PCS As String
	Dim LOADING_SPEECH_CASE As String
	Dim LOADING_SPEECH_BOX As String
	Dim LOADING_SPEECH_DOZ As String
	Dim LOADING_SPEECH_PACK As String
	Dim LOADING_SPEECH_BAG As String

	If LABEL_CASE_QTY_LOADING.Text > 0 Then
		If LABEL_PCS_QTY_LOADING.Text > 0 Or _
						LABEL_BOX_QTY_LOADING.Text > 0 Or _
						LABEL_DOZ_QTY_LOADING.Text > 0 Or _
						LABEL_PACK_QTY_LOADING.Text > 0 Or _
						LABEL_BAG_QTY_LOADING.Text > 0 Then
			LOADING_SPEECH_CASE = LABEL_CASE_QTY_LOADING.Text & " CASE and "
		Else
			LOADING_SPEECH_CASE = LABEL_CASE_QTY_LOADING.Text & " CASE. "
		End If
	Else
		LOADING_SPEECH_CASE = " "
	End If
	Sleep(0)
	If LABEL_PCS_QTY_LOADING.Text > 0 Then
		If LABEL_BOX_QTY_LOADING.Text > 0 Or _
						LABEL_DOZ_QTY_LOADING.Text > 0 Or _
						LABEL_PACK_QTY_LOADING.Text > 0 Or _
						LABEL_BAG_QTY_LOADING.Text > 0 Then
			LOADING_SPEECH_PCS = LABEL_PCS_QTY_LOADING.Text & " PIECES and "
		Else
			LOADING_SPEECH_PCS = LABEL_PCS_QTY_LOADING.Text & " PIECES. "
		End If
	Else
		LOADING_SPEECH_PCS = " "
	End If
	Sleep(0)
	If LABEL_BOX_QTY_LOADING.Text > 0 Then
				
		If LABEL_DOZ_QTY_LOADING.Text > 0 Or _
						LABEL_PACK_QTY_LOADING.Text > 0 Or _
						LABEL_BAG_QTY_LOADING.Text > 0 Then
			LOADING_SPEECH_BOX = LABEL_BOX_QTY_LOADING.Text & " BOX and "
		Else
			LOADING_SPEECH_BOX = LABEL_BOX_QTY_LOADING.Text & " BOX. "
		End If
	Else
		LOADING_SPEECH_BOX = " "
	End If
	Sleep(0)
	If LABEL_DOZ_QTY_LOADING.Text > 0 Then
		If LABEL_PACK_QTY_LOADING.Text > 0 Or _
						LABEL_BAG_QTY_LOADING.Text > 0 Then
			LOADING_SPEECH_DOZ = LABEL_DOZ_QTY_LOADING.Text & " DOZEN and "
		Else
			LOADING_SPEECH_DOZ = LABEL_DOZ_QTY_LOADING.Text & " DOZEN. "
		End If
	Else
		LOADING_SPEECH_DOZ = " "
	End If
	Sleep(0)
	If LABEL_PACK_QTY_LOADING.Text > 0 Then
		If LABEL_BAG_QTY_LOADING.Text > 0 Then
			LOADING_SPEECH_PACK = LABEL_PACK_QTY_LOADING.Text & " PACK and "
		Else
			LOADING_SPEECH_PACK = LABEL_PACK_QTY_LOADING.Text & " PACK. "
		End If
	Else
		LOADING_SPEECH_PACK = " "
	End If
	Sleep(0)
	If LABEL_BAG_QTY_LOADING.Text > 0 Then
		LOADING_SPEECH_BAG = LABEL_BAG_QTY_LOADING.Text & " BAG. "
	Else
		LOADING_SPEECH_BAG = " "
	End If
	Sleep(20)
	TTS1.Speak(LOADING_SPEECH_CASE _
					 & LOADING_SPEECH_PCS _
					 & LOADING_SPEECH_BOX _
					  & LOADING_SPEECH_DOZ _
					  & LOADING_SPEECH_PACK _
					   & LOADING_SPEECH_BAG, True)
	Sleep(20)
	TTS1.Speak(LOADING_SPEECH_CASE & LOADING_SPEECH_PCS & LOADING_SPEECH_BOX & LOADING_SPEECH_DOZ & LOADING_SPEECH_PACK & LOADING_SPEECH_BAG, True)
End Sub

Sub GET_PRODUCT_DETAILS
	cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESC_LOADING.Text&"'")
	If cursor3.RowCount > 0 Then
		For qrow = 0 To cursor3.RowCount - 1
			cursor3.Position = qrow
			LABEL_LOAD_VARIANT_LOADING.Text = cursor3.GetString("product_variant")
			principal_id = cursor3.GetString("principal_id")
			product_id = cursor3.GetString("product_id")
			
			caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
			pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
			dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
			boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
			bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
			packper = cursor3.GetString("PACK_UNIT_PER_PCS")
			
			
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
Sub GET_TOTAL_SERVED
	
	cursor7 = connection.ExecQuery("SELECT * FROM picklist_LOADING_disc_table WHERE product_description ='"&LABEL_LOAD_DESC_LOADING.Text&"' AND picklist_id = '"&picklist_id&"'")
	If cursor7.RowCount > 0 Then
		For row = 0 To cursor7.RowCount - 1
			cursor7.Position = row
			If cursor7.GetString("unit") = "CASE" Then
				EDITTEXT_CASE_QTY_LOADING.Text = cursor7.GetString("loaded_quantity")
				BUTTON_CASE_LOADING.Text = "UPDATE"
				BUTTON_CASE_LOADING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "PCS" Then
				EDITTEXT_PCS_QTY_LOADING.Text = cursor7.GetString("loaded_quantity")
				BUTTON_PCS_LOADING.Text = "UPDATE"
				BUTTON_PCS_LOADING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "BOX" Then
				EDITTEXT_BOX_QTY_LOADING.Text = cursor7.GetString("loaded_quantity")
				BUTTON_BOX_LOADING.Text = "UPDATE"
				BUTTON_BOX_LOADING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "DOZ" Then
				EDITTEXT_DOZ_QTY_LOADING.Text = cursor7.GetString("loaded_quantity")
				BUTTON_DOZ_LOADING.Text = "UPDATE"
				BUTTON_DOZ_LOADING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "BAG" Then
				EDITTEXT_BAG_QTY_LOADING.Text = cursor7.GetString("loaded_quantity")
				BUTTON_BAG_LOADING.Text = "UPDATE"
				BUTTON_BAG_LOADING.Background = BG_UPDATE
			End If
			If cursor7.GetString("unit") = "PACK" Then
				EDITTEXT_PACK_QTY_LOADING.Text = cursor7.GetString("loaded_quantity")
				BUTTON_PACK_LOADING.Text = "UPDATE"
				BUTTON_PACK_LOADING.Background = BG_UPDATE
			End If
		Next
		
		
	End If
End Sub
Sub CLEAR_PICKLIST_SKU
	LABEL_CASE_QTY_LOADING.Text = "0"
	LABEL_PCS_QTY_LOADING.Text = "0"
	LABEL_BOX_QTY_LOADING.Text = "0"
	LABEL_DOZ_QTY_LOADING.Text = "0"
	LABEL_BAG_QTY_LOADING.Text = "0"
	LABEL_PACK_QTY_LOADING.Text = "0"
	EDITTEXT_CASE_QTY_LOADING.Text = ""
	EDITTEXT_PCS_QTY_LOADING.Text = ""
	EDITTEXT_BOX_QTY_LOADING.Text = ""
	EDITTEXT_DOZ_QTY_LOADING.Text = ""
	EDITTEXT_PACK_QTY_LOADING.Text = ""
	EDITTEXT_BAG_QTY_LOADING.Text = ""
	BUTTON_BAG_LOADING.Text = "LOAD"
	BUTTON_BOX_LOADING.Text = "LOAD"
	BUTTON_CASE_LOADING.Text = "LOAD"
	BUTTON_DOZ_LOADING.Text = "LOAD"
	BUTTON_PACK_LOADING.Text = "LOAD"
	BUTTON_PCS_LOADING.Text = "LOAD"
	DISABLE_BUTTON
End Sub
'process
Sub LOADING_PCS
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_PCS_QTY_LOADING.Text * pcsper
	
	Dim LOADING_status As String
	
	If EDITTEXT_PCS_QTY_LOADING.Text <> LABEL_PCS_QTY_LOADING.Text Then
		LOADING_status = "ON HOLD"
	Else
		LOADING_status = "LOADED"
	End If
	Dim SPEECH1 As String
	If BUTTON_PCS_LOADING.Text = "LOAD" Then
		SPEECH1 = "LOADED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_LOADING.Text,"PCS"))
	Sleep(0)
	Dim query As String = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_LOADING.Text,LABEL_LOAD_DESC_LOADING.Text,"PCS",LABEL_PCS_QTY_LOADING.Text,EDITTEXT_PCS_QTY_LOADING.Text,total_pieces _
	,LOADING_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0))
	ToastMessageShow(EDITTEXT_PCS_QTY_LOADING.Text & " " & "PIECE(S) " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_PCS_QTY_LOADING.Text & " " & "PIECES " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub LOADING_CASE
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_CASE_QTY_LOADING.Text * caseper
	
	Dim LOADING_status As String
	
	If EDITTEXT_CASE_QTY_LOADING.Text <> LABEL_CASE_QTY_LOADING.Text Then
		LOADING_status = "ON HOLD"
	Else
		LOADING_status = "LOADED"
	End If
	
	Dim SPEECH1 As String
	If BUTTON_CASE_LOADING.Text = "LOAD" Then
		SPEECH1 = "LOADED"
	Else
		SPEECH1 = "UPDATED"
	End If

	Dim query As String = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_LOADING.Text,"CASE"))
	Sleep(0)
	Dim query As String = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_LOADING.Text,LABEL_LOAD_DESC_LOADING.Text,"CASE",LABEL_CASE_QTY_LOADING.Text,EDITTEXT_CASE_QTY_LOADING.Text,total_pieces _
	,LOADING_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0))
	ToastMessageShow(EDITTEXT_CASE_QTY_LOADING.Text & " " & "CASE " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_CASE_QTY_LOADING.Text & " " & "CASE " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub LOADING_BOX
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_BOX_QTY_LOADING.Text * boxper
	
	Dim LOADING_status As String
	
	If EDITTEXT_BOX_QTY_LOADING.Text <> LABEL_BOX_QTY_LOADING.Text Then
		LOADING_status = "ON HOLD"
	Else
		LOADING_status = "LOADED"
	End If
	Dim SPEECH1 As String
	If BUTTON_BOX_LOADING.Text = "LOAD" Then
		SPEECH1 = "LOADED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_LOADING.Text,"BOX"))
	Sleep(0)
	Dim query As String = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
		,product_id,LABEL_LOAD_VARIANT_LOADING.Text,LABEL_LOAD_DESC_LOADING.Text,"BOX",LABEL_BOX_QTY_LOADING.Text,EDITTEXT_BOX_QTY_LOADING.Text,total_pieces _
		,LOADING_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0))
	ToastMessageShow(EDITTEXT_BOX_QTY_LOADING.Text & " " & "BOX " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_BOX_QTY_LOADING.Text & " " & "BOX " & SPEECH1, False)
	
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub LOADING_DOZ
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_DOZ_QTY_LOADING.Text * dozper
	
	Dim LOADING_status As String
	
	If EDITTEXT_DOZ_QTY_LOADING.Text <> LABEL_DOZ_QTY_LOADING.Text Then
		LOADING_status = "ON HOLD"
	Else
		LOADING_status = "LOADED"
	End If
	Dim SPEECH1 As String
	If BUTTON_BOX_LOADING.Text = "LOADED" Then
		SPEECH1 = "LOADING"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_LOADING.Text,"DOZ"))
	Sleep(0)
	Dim query As String = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_LOADING.Text,LABEL_LOAD_DESC_LOADING.Text,"DOZ",LABEL_DOZ_QTY_LOADING.Text,EDITTEXT_DOZ_QTY_LOADING.Text,total_pieces _
	,LOADING_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0))
	ToastMessageShow(EDITTEXT_DOZ_QTY_LOADING.Text & " " & "DOZEN " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_DOZ_QTY_LOADING.Text & " " & "DOZEN " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub LOADING_PACK
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_PACK_QTY_LOADING.Text * packper
	
	Dim LOADING_status As String
	
	If EDITTEXT_PACK_QTY_LOADING.Text <> LABEL_PACK_QTY_LOADING.Text Then
		LOADING_status = "ON HOLD"
	Else
		LOADING_status = "LOADED"
	End If
	Dim SPEECH1 As String
	If BUTTON_PACK_LOADING.Text = "LOAD" Then
		SPEECH1 = "LOADED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_LOADING.Text,"PACK"))
	Sleep(0)
	Dim query As String = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_LOADING.Text,LABEL_LOAD_DESC_LOADING.Text,"PACK",LABEL_PACK_QTY_LOADING.Text,EDITTEXT_PACK_QTY_LOADING.Text,total_pieces _
	,LOADING_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0))
	ToastMessageShow(EDITTEXT_PACK_QTY_LOADING.Text & " " & "PACK " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_PACK_QTY_LOADING.Text & " " & "PACK " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
Sub LOADING_BAG
	ProgressDialogShow2("Loading...", False)
	total_pieces = EDITTEXT_BAG_QTY_LOADING.Text * bagper
	
	Dim LOADING_status As String
	
	If EDITTEXT_BAG_QTY_LOADING.Text <> LABEL_BAG_QTY_LOADING.Text Then
		LOADING_status = "ON HOLD"
	Else
		LOADING_status = "LOADED"
	End If
	
	Dim SPEECH1 As String
	If BUTTON_BAG_LOADING.Text = "LOADED" Then
		SPEECH1 = "LOADED"
	Else
		SPEECH1 = "UPDATED"
	End If
	Dim query As String = "DELETE FROM picklist_LOADING_disc_table WHERE picklist_id = ? AND product_description = ? AND unit = ?"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_DESC_LOADING.Text,"BAG"))
	Sleep(0)
	Dim query As String = "INSERT INTO picklist_LOADING_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text,principal_id,principal_name _
	,product_id,LABEL_LOAD_VARIANT_LOADING.Text,LABEL_LOAD_DESC_LOADING.Text,"BAG",LABEL_BAG_QTY_LOADING.Text,EDITTEXT_BAG_QTY_LOADING.Text,total_pieces _
	,LOADING_status,LOGIN_MODULE.username,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),scan_code,reason,LOGIN_MODULE.tab_id,0))
	ToastMessageShow(EDITTEXT_BAG_QTY_LOADING.Text & " " & "BAG " & SPEECH1, False)
	TTS1.Speak(EDITTEXT_BAG_QTY_LOADING.Text & " " & "BAG " & SPEECH1, False)
	Sleep(0)
	GET_TOTAL_SERVED
	CTRL.HideKeyboard
	ProgressDialogHide
	scan_next_trigger = 0
End Sub
'buttonclick
Sub BUTTON_PCS_LOADING_Click
	If EDITTEXT_PCS_QTY_LOADING.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you LOADING, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		CMB_UNIT.cmbBox.Clear
		CMB_UNIT.cmbBox.Add("PCS")
		If EDITTEXT_PCS_QTY_LOADING.Text > LABEL_PCS_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = EDITTEXT_PCS_QTY_LOADING.Text - LABEL_PCS_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			OVER_PREPARE
		Else If EDITTEXT_PCS_QTY_LOADING.Text < LABEL_PCS_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = LABEL_PCS_QTY_LOADING.Text - EDITTEXT_PCS_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			SHORT_PREPARE
		Else
			If unit_trigger = 1 Then
				Msgbox2Async("The unit " & LABEL_LOAD_SCANCODE.Text &" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?", "Unit not in order!", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					LOADING_PCS
				End If
			Else
				LOADING_PCS
			End If
		End If
	End If
End Sub
Sub BUTTON_CASE_LOADING_Click
	Sleep(0)
	If EDITTEXT_CASE_QTY_LOADING.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you LOADING, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		CMB_UNIT.cmbBox.Clear
		CMB_UNIT.cmbBox.Add("CASE")
		If EDITTEXT_CASE_QTY_LOADING.Text > LABEL_CASE_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = EDITTEXT_CASE_QTY_LOADING.Text - LABEL_CASE_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			OVER_PREPARE
		Else If EDITTEXT_CASE_QTY_LOADING.Text < LABEL_CASE_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = LABEL_CASE_QTY_LOADING.Text - EDITTEXT_CASE_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			SHORT_PREPARE
		Else
			If unit_trigger = 1 Then
				Msgbox2Async("The unit " & LABEL_LOAD_SCANCODE.Text &" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?", "Unit not in order!", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					LOADING_CASE
				End If
			Else
				LOADING_CASE
			End If
		End If
	End If
End Sub
Sub BUTTON_BOX_LOADING_Click
	If EDITTEXT_BOX_QTY_LOADING.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you LOADING, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		CMB_UNIT.cmbBox.Clear
		CMB_UNIT.cmbBox.Add("BOX")
		If EDITTEXT_BOX_QTY_LOADING.Text > LABEL_BOX_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = EDITTEXT_BOX_QTY_LOADING.Text - LABEL_BOX_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			OVER_PREPARE
		Else If EDITTEXT_BOX_QTY_LOADING.Text > LABEL_BOX_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = LABEL_BOX_QTY_LOADING.Text - EDITTEXT_BOX_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			SHORT_PREPARE
		Else
			
			If unit_trigger = 1 Then
				Msgbox2Async("The unit " & LABEL_LOAD_SCANCODE.Text &" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?", "Unit not in order!", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					LOADING_BOX
				End If
			Else
				LOADING_BOX
			End If
		End If
	End If
End Sub
Sub BUTTON_DOZ_LOADING_Click
	If EDITTEXT_DOZ_QTY_LOADING.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you LOADING, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		CMB_UNIT.cmbBox.Clear
		CMB_UNIT.cmbBox.Add("DOZ")
		If EDITTEXT_DOZ_QTY_LOADING.Text > LABEL_DOZ_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = EDITTEXT_DOZ_QTY_LOADING.Text - LABEL_DOZ_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			OVER_PREPARE
		Else If EDITTEXT_DOZ_QTY_LOADING.Text < LABEL_DOZ_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = LABEL_DOZ_QTY_LOADING.Text - EDITTEXT_DOZ_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			SHORT_PREPARE
		Else
			If unit_trigger = 1 Then
				Msgbox2Async("The unit " & LABEL_LOAD_SCANCODE.Text &" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?", "Unit not in order!", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					LOADING_DOZ
				End If
			Else
				LOADING_DOZ
			End If
		End If
	End If
End Sub
Sub BUTTON_PACK_LOADING_Click
	If EDITTEXT_PACK_QTY_LOADING.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you LOADING, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		CMB_UNIT.cmbBox.Clear
		CMB_UNIT.cmbBox.Add("PACK")
		If EDITTEXT_PACK_QTY_LOADING.Text > LABEL_PACK_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = EDITTEXT_PACK_QTY_LOADING.Text - LABEL_PACK_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			OVER_PREPARE
		Else If EDITTEXT_PACK_QTY_LOADING.Text < LABEL_PACK_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = LABEL_PACK_QTY_LOADING.Text - EDITTEXT_PACK_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			SHORT_PREPARE
		Else
			If unit_trigger = 1 Then
				Msgbox2Async("The unit " & LABEL_LOAD_SCANCODE.Text &" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?", "Unit not in order!", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					LOADING_PACK
				End If
			Else
				LOADING_PACK
			End If
		End If
	End If
End Sub
Sub BUTTON_BAG_LOADING_Click
	If EDITTEXT_BAG_QTY_LOADING.Text = "" Then
		Msgbox2Async("Please input a quantity to the unit you LOADING, Empty value cannot proceed.", "Null Quantity!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		CMB_UNIT.cmbBox.Clear
		CMB_UNIT.cmbBox.Add("BAG")
		If EDITTEXT_BAG_QTY_LOADING.Text > LABEL_BAG_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = EDITTEXT_BAG_QTY_LOADING.Text - LABEL_BAG_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			OVER_PREPARE
		Else if EDITTEXT_BAG_QTY_LOADING.Text < LABEL_BAG_QTY_LOADING.Text Then
			EDITTEXT_QUANTITY.Text = LABEL_BAG_QTY_LOADING.Text - EDITTEXT_BAG_QTY_LOADING.Text
			EDITTEXT_QUANTITY.Enabled = False
			SHORT_PREPARE
		Else	
			If unit_trigger = 1 Then
				Msgbox2Async("The unit " & LABEL_LOAD_SCANCODE.Text &" you scan is not ordered in the picklist, Please check carefully the unit you loading before you proceed. Would you like to continue?", "Unit not in order!", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					LOADING_BAG
				End If
			Else
				LOADING_BAG
			End If
		End If
	End If
End Sub
'focuschange
Sub EDITTEXT_BAG_QTY_LOADING_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_BAG_QTY_LOADING.SelectAll
	Else
	End If
End Sub
Sub EDITTEXT_PACK_QTY_LOADING_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_PACK_QTY_LOADING.SelectAll
	Else
	End If
End Sub
Sub EDITTEXT_DOZ_QTY_LOADING_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_DOZ_QTY_LOADING.SelectAll
	Else
		
	End If
End Sub
Sub EDITTEXT_BOX_QTY_LOADING_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_BOX_QTY_LOADING.SelectAll
	Else
		
	End If
End Sub
Sub EDITTEXT_CASE_QTY_LOADING_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_CASE_QTY_LOADING.SelectAll
	Else
		
	End If
End Sub
Sub EDITTEXT_PCS_QTY_LOADING_FocusChanged (HasFocus As Boolean)
	If HasFocus = True Then
		EDITTEXT_PCS_QTY_LOADING.SelectAll
	Else
		
	End If
End Sub

Sub BUTTON_EXIT_LOADING_Click
	CLEAR_PICKLIST_SKU
	Sleep(0)
	PANEL_BG_LOADING.SetVisibleAnimated(300, False)
	CTRL.HideKeyboard
	XSelections.Clear
	scan_next_trigger = 0
	Sleep(0)
	LOAD_LOADING_PICKLIST
End Sub
Sub PANEL_BG_LOADING_Click
	Return True
End Sub

Sub GET_PREPARED_DETAILS
	CMB_PICKER.cmbBox.Clear
	
	cursor1 = connection.ExecQuery("SELECT User FROM users_table WHERE Position = 'LOGISTIC PERSONEL' ORDER BY User ASC")
	If cursor1.RowCount > 0 Then
		For i = 0 To cursor1.RowCount - 1
			cursor1.Position = i
			Sleep(100)
			Dim picker As String = "%"& cursor1.GetString("User") &"%"

			Dim req As DBRequestManager = CreateRequest
			Dim cmd As DBCommand = CreateCommand("select_picklist_prepared", Array(picklist_id,picker))
			Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
			If jr.Success Then
				req.HandleJobAsync(jr, "req")
				Wait For (req) req_Result(res As DBResult)
				If res.Rows.Size > 0 Then
					For Each row() As Object In res.Rows
					Next
					CMB_PICKER.cmbBox.Add(cursor1.GetString("User"))
					LABEL_LOAD_CHECKER.Text = row(res.Columns.Get("prepared_by"))
				Else
			
				End If
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				Log("ERROR: " & jr.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Updating Error", False)
			End If
			jr.Release

		Next
		
		
	End If
End Sub
Sub GET_VISOR
	CMB_SUPERVISOR.cmbBox.Clear
	cursor6 = connection.ExecQuery("SELECT User FROM users_table WHERE Position LIKE '%LOGISTIC OFFICER%' ORDER BY User ASC")
	For i = 0 To cursor6.RowCount - 1
		Sleep(0)
		cursor6.Position = i
		CMB_SUPERVISOR.cmbBox.Add(cursor6.GetString("User"))
	Next
End Sub
Sub LOAD_REASON
	CMB_REASON.cmbBox.Enabled = True
	CMB_REASON.cmbBox.Clear
	CMB_REASON.cmbBox.Add("WRONG SELECTION")
	CMB_REASON.cmbBox.Add("WRONG SIZE")
	CMB_REASON.cmbBox.Add("WRONG FLAVOR/COLOR")
	CMB_REASON.cmbBox.Add("WRONG SKU")
	CMB_REASON.cmbBox.Add("WRONG SCAN")
End Sub
Sub GET_UNIT
	CMB_UNIT.cmbBox.Enabled = True
	
	cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESC_LOADING.Text&"'")
	For qrow = 0 To cursor3.RowCount - 1
		cursor3.Position = qrow
		product_id = cursor3.GetString("product_id")
		LABEL_LOAD_VARIANT_LOADING.Text = cursor3.GetString("product_variant")
		
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
	CMB_SUPERVISOR.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_SUPERVISOR.cmbBox)
End Sub
Sub CMB_SUPERVISOR_SelectedIndexChanged (Index As Int)
	EDITTEXT_TRAIL_PASS.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_TRAIL_PASS)
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
		cursor2 = connection.ExecQuery("SELECT * FROM users_table WHERE user = '"&CMB_SUPERVISOR.cmbBox.SelectedItem&"' and pass = '"&EDITTEXT_TRAIL_PASS.Text&"'")
		If cursor2.RowCount > 0 Then
			ProgressDialogShow2("Saving...",False)
			If CMB_PICKER.cmbBox.SelectedIndex = -1 Then CMB_PICKER.cmbBox.SelectedIndex = 0
			If CMB_REASON.cmbBox.SelectedIndex = -1 Then CMB_REASON.cmbBox.SelectedIndex = 0
			If CMB_UNIT.cmbBox.SelectedIndex = -1 Then CMB_UNIT.cmbBox.SelectedIndex = 0

			Dim query As String = "INSERT INTO picklist_table_trail VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text _
		,product_id,LABEL_LOAD_VARIANT_LOADING.Text,LABEL_LOAD_DESC_LOADING.Text,CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QUANTITY.Text _
		,CMB_REASON.cmbBox.SelectedItem & " " & CMB_SUPERVISOR.cmbBox.SelectedItem,LABEL_LOAD_CHECKER.Text,LOGIN_MODULE.username,CMB_PICKER.cmbBox.SelectedItem,DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now),"LOADING",LOGIN_MODULE.tab_id))
			Sleep(0)
		
			PANEL_BG_TRAIL.SetVisibleAnimated(300, False)
		
			If over_short_trigger = 1 Then
				Msgbox2Async("Over prepared reported! Please bring back the over items to the warehouse and load only the corresponding order quantity.", "Notice", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
			
				If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
					EDITTEXT_PCS_QTY_LOADING.Text = LABEL_PCS_QTY_LOADING.Text
					Sleep(0)
					LOADING_PCS
				else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
					EDITTEXT_CASE_QTY_LOADING.Text = LABEL_CASE_QTY_LOADING.Text
					Sleep(0)
					LOADING_CASE
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
					EDITTEXT_BOX_QTY_LOADING.Text = LABEL_BOX_QTY_LOADING.Text
					Sleep(0)
					LOADING_BOX
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
					EDITTEXT_DOZ_QTY_LOADING.Text = LABEL_DOZ_QTY_LOADING.Text
					Sleep(0)
					LOADING_DOZ
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
					EDITTEXT_PACK_QTY_LOADING.Text = LABEL_PACK_QTY_LOADING.Text
					Sleep(0)
					LOADING_PACK
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
					EDITTEXT_BAG_QTY_LOADING.Text = LABEL_BAG_QTY_LOADING.Text
					Sleep(0)
					LOADING_BAG
				End If
			Else If over_short_trigger = 2 Then
				Msgbox2Async("Short prepared reported! Please call the picker of this pickist and check strictly if the item quantity really short before getting the shorted item.", "Notice", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
			
				If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
					Sleep(0)
					LOADING_PCS
				else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
					Sleep(0)
					LOADING_CASE
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
					Sleep(0)
					LOADING_BOX
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
					Sleep(0)
					LOADING_DOZ
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
					Sleep(0)
					LOADING_PACK
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
					Sleep(0)
					LOADING_BAG
				End If
			End If
			ProgressDialogHide
			scan_trigger = 0
		Else
			ToastMessageShow("Wrong Password", False)
		End If
	End If
End Sub

Sub OPEN_DELIVERY
	PANEL_BG_DELIVERY.SetVisibleAnimated(300, True)
	PANEL_BG_DELIVERY.BringToFront
	EDITTEXT_PLATE.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_PLATE)
End Sub
Sub GET_DELIVERY_DETAILS
	
	cursor8 = connection.ExecQuery("SELECT * FROM picklist_delivery_report_table WHERE picklist_id = '"&picklist_id&"'")
	If cursor8.RowCount > 0 Then
		For i = 0 To cursor8.RowCount - 1
			cursor8.Position = i
			Sleep(0)
			EDITTEXT_PLATE.Text = cursor8.GetString("plate_no")
			EDITTEXT_PUSHCART.Text = cursor8.GetString("pushcart")
			EDITTEXT_DRIVER.Text = cursor8.GetString("driver")
			EDITTEXT_HELPER1.Text = cursor8.GetString("helper1")
			EDITTEXT_HELPER2.Text = cursor8.GetString("helper2")
			EDITTEXT_HELPER3.Text = cursor8.GetString("helper3")
		Next
	Else
		ToastMessageShow("No existing picklist delivery", False)
	End If
End Sub
Sub BUTTON_DELIVERY_Click
	OPEN_DELIVERY
	Sleep(0)
	GET_DELIVERY_DETAILS
End Sub
Sub BUTTON_SEARCH_Click
	
	cursor2 = connection.ExecQuery("SELECT * FROM picklist_delivery_report_table WHERE plate_no = '"&EDITTEXT_PLATE.Text&"' ORDER BY date_time_registered DESC LIMIT 1")
	If cursor2.RowCount > 0 Then
		For i = 0 To cursor2.RowCount - 1
			cursor2.Position = i
			Sleep(0)
			EDITTEXT_PLATE.Text = cursor2.GetString("plate_no")
			EDITTEXT_PUSHCART.Text = cursor2.GetString("pushcart")
			EDITTEXT_DRIVER.Text = cursor2.GetString("driver")
			EDITTEXT_HELPER1.Text = cursor2.GetString("helper1")
			EDITTEXT_HELPER2.Text = cursor2.GetString("helper2")
			EDITTEXT_HELPER3.Text = cursor2.GetString("helper3")
		Next
		CTRL.HideKeyboard
		
		
	Else
		ToastMessageShow("No delivery history for this plate no.", False)
	End If
End Sub
Sub BUTTON_SAVE_Click
	If EDITTEXT_PLATE.Text = "" Then
		ToastMessageShow("Please input a plate no.", False)
		EDITTEXT_PLATE.RequestFocus
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_PLATE)
	Else If EDITTEXT_PLATE.Text.Length < 6 Then
		ToastMessageShow("Plate no. must be 6 or higher letter/numbers.", False)
		EDITTEXT_PLATE.RequestFocus
		EDITTEXT_PLATE.SelectAll
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_PLATE)
	Else If EDITTEXT_DRIVER.Text = "" Then
		ToastMessageShow("Please input a driver.", False)
		EDITTEXT_DRIVER.RequestFocus
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_DRIVER)
	Else If EDITTEXT_HELPER1.Text = "" Then
		ToastMessageShow("Please input atleast helper #1.", False)
		EDITTEXT_HELPER1.RequestFocus
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_HELPER1)
	Else If EDITTEXT_PUSHCART.Text = "" Then
		ToastMessageShow("Please input a pushcart count.", False)
		EDITTEXT_PUSHCART.RequestFocus
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_PUSHCART)
	Else
		ProgressDialogShow2("Saving...",False)
		connection.ExecNonQuery("DELETE FROM picklist_delivery_report_table WHERE picklist_id = '"&picklist_id&"'")
		Sleep(0)
		Dim query As String = "INSERT INTO picklist_delivery_report_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
		connection.ExecNonQuery2(query,Array As String(picklist_id,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text _
		,EDITTEXT_PLATE.Text,EDITTEXT_DRIVER.Text,EDITTEXT_HELPER1.Text,EDITTEXT_HELPER2.Text,EDITTEXT_HELPER3.Text _
		,EDITTEXT_PUSHCART.Text,LOGIN_MODULE.username,LOGIN_MODULE.tab_id,DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now)))
		
		
		
		PANEL_BG_DELIVERY.SetVisibleAnimated(300, False)
		CTRL.HideKeyboard
		ProgressDialogHide
	End If
End Sub

Sub OVER_PREPARE
	over_short_trigger = 1
	Msgbox2Async("The product :"& CRLF &""&LABEL_LOAD_DESC_LOADING.Text&" "& CRLF &"IS OVER PREPARE, Please register this report after you click OK.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	PANEL_BG_TRAIL.SetVisibleAnimated(300,True)
	PANEL_BG_TRAIL.BringToFront
	GET_VISOR
	CMB_REASON.cmbBox.Enabled = False
	CMB_UNIT.cmbBox.Enabled = False
	CMB_REASON.cmbBox.Clear
	Sleep(0)
	CMB_REASON.cmbBox.Add("OVER PREPARE")
	CMB_PICKER.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_PICKER.cmbBox)
	CTRL.HideKeyboard
	EDITTEXT_QUANTITY.Text = EDITTEXT_QUANTITY.Text.SubString2(0,EDITTEXT_QUANTITY.Text.IndexOf("."))
End Sub
Sub SHORT_PREPARE
	over_short_trigger = 2
	Msgbox2Async("The product :"& CRLF &""&LABEL_LOAD_DESC_LOADING.Text&" "& CRLF &"IS SHORT PREPARE, Please register this report after you click OK.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	PANEL_BG_TRAIL.SetVisibleAnimated(300,True)
	GET_VISOR
	CMB_REASON.cmbBox.Enabled = False
	CMB_UNIT.cmbBox.Enabled = False
	CMB_REASON.cmbBox.Clear
	Sleep(0)
	CMB_REASON.cmbBox.Add("SHORT PREPARE")
	CMB_PICKER.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_PICKER.cmbBox)
	CTRL.HideKeyboard
	EDITTEXT_QUANTITY.Text = EDITTEXT_QUANTITY.Text.SubString2(0,EDITTEXT_QUANTITY.Text.IndexOf("."))
End Sub

Sub DELETE_PICKLIST_LOADING
	PANEL_BG_TYPE.SetVisibleAnimated(300,False)
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
	LABEL_HEADER_TEXT.Text = "Uploading Picklist"
	LABEL_MSGBOX2.Text = "Data getting ready.."
	Dim cmd As DBCommand = CreateCommand("delete_picklist_loaded", Array(picklist_id.Trim))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_PICKLIST_LOADING
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
	End If
	js.Release
End Sub
Sub INSERT_PICKLIST_LOADING
	Sleep(0)
	
	cursor3 = connection.ExecQuery("SELECT * FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id.Trim&"' GROUP BY product_id, unit")
	If cursor3.RowCount > 0 Then
		For i3 = 0 To cursor3.RowCount - 1
			cursor3.Position = i3
			Dim cmd As DBCommand = CreateCommand("insert_picklist_loaded", Array As String(cursor3.GetString("picklist_id"), cursor3.GetString("picklist_name"), cursor3.GetString("picklist_date"), _
			cursor3.GetString("principal_id"),cursor3.GetString("principal_name"), cursor3.GetString("product_id"),cursor3.GetString("product_variant"),cursor3.GetString("product_description"), _
			cursor3.GetString("unit"), cursor3.GetString("quantity"),  cursor3.GetString("loaded_quantity"),cursor3.GetString("loaded_total_pcs"), cursor3.GetString("loaded_status"), _
			cursor3.GetString("load_by"), cursor3.GetString("load_date"),  cursor3.GetString("load_time"),cursor3.GetString("scan_code"), cursor3.GetString("reason")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Uploading :" & cursor3.GetString("product_description")
			Else
				Log("ERROR: " & js.ErrorMessage)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next
		CHECK_PICKLIST_LOADED
	End If
End Sub
Sub CHECK_PICKLIST_LOADED
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_count_picklist_loaded", Array(picklist_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				loaded_count = row(res.Columns.Get("loaded_count"))
			Next
		Else
			loaded_count = 0
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
	Sleep(100)
	cursor8 = connection.ExecQuery("SELECT count(product_id) as 'uploaded_count' FROM picklist_loading_disc_table WHERE picklist_id = '"&picklist_id.Trim&"'")
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
	LABEL_MSGBOX2.Text = "Total Served : " & loaded_count & " " & "Total Uploaded : " & uploaded_count
	Sleep(2000)
	If uploaded_count <> loaded_count Then
		ToastMessageShow("Uploading Order Not Complete. Reuploading", False)
		Sleep(0)
		DELETE_PICKLIST_LOADING
	Else
		DELETE_PICKLIST_LOADED_TRAIL
	End If
End Sub
Sub DELETE_PICKLIST_LOADED_TRAIL
	Dim cmd As DBCommand = CreateCommand("delete_picklist_trail", Array(picklist_id.Trim,"LOADING"))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_PICKLIST_LOADED_TRAIL
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
	End If
	js.Release
End Sub
Sub INSERT_PICKLIST_LOADED_TRAIL
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
				Log("ERROR: " & js.ErrorMessage)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next
		
		
	End If
	Sleep(0)
	DELETE_DISPATCH
End Sub
Sub DELETE_DISPATCH
	Dim cmd As DBCommand = CreateCommand("delete_dispatch", Array(picklist_id.Trim))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		INSERT_DISPATCH
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & js.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	js.Release
End Sub
Sub INSERT_DISPATCH
	GET_DELIVERY_DETAILS
	Sleep(0)
	PANEL_BG_DELIVERY.SetVisibleAnimated(300, False)
	CTRL.HideKeyboard
	Sleep(0)
	Dim cmd As DBCommand = CreateCommand("insert_dispatch", Array(picklist_id.trim,LABEL_LOAD_NAME.Text,LABEL_LOAD_DATE.Text, _
		"-",EDITTEXT_PLATE.Text.Trim.ToUpperCase, EDITTEXT_DRIVER.Text.ToUpperCase , EDITTEXT_HELPER1.Text.ToUpperCase, EDITTEXT_HELPER2.Text.ToUpperCase, EDITTEXT_HELPER3.Text.ToUpperCase, DateTime.Date(DateTime.now) & " " & DateTime.time(DateTime.now) _
		, "-", "-", "-", "-", "-", "-", "-", "-"))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		UPDATE_LOADED
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & js.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	js.Release
End Sub
Sub UPDATE_LOADED
	LABEL_MSGBOX2.Text = "Updating Status..."
	Dim cmd As DBCommand = CreateCommand("update_picklist_loaded", Array(DateTime.Date(DateTime.Now) &" "&DateTime.Time(DateTime.Now), picklist_id.Trim))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		LABEL_MSGBOX2.Text = "Picklist uploaded succesfully..."
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Msgbox2Async("Picklist loaded and uploaded successfully.", "Notice", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		CLEAR_PICKLIST
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & js.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	js.Release
End Sub

Sub BUTTON_UPLOAD_Click
	Msgbox2Async("Are you sure you want to UPLOAD this picklist?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		DELETE_PICKLIST_LOADING
	Else
		
	End If
End Sub

Sub DISABLE_BUTTON
	BUTTON_CASE_LOADING.Enabled = False
	BUTTON_CASE_LOADING.Background = BG_DISABLE
	BUTTON_PCS_LOADING.Enabled = False
	BUTTON_PCS_LOADING.Background = BG_DISABLE
	BUTTON_BOX_LOADING.Enabled = False
	BUTTON_BOX_LOADING.Background = BG_DISABLE
	BUTTON_DOZ_LOADING.Enabled = False
	BUTTON_DOZ_LOADING.Background = BG_DISABLE
	BUTTON_PACK_LOADING.Enabled = False
	BUTTON_PACK_LOADING.Background = BG_DISABLE
	BUTTON_BAG_LOADING.Enabled = False
	BUTTON_BAG_LOADING.Background = BG_DISABLE
	
End Sub

Sub EDITTEXT_QUANTITY_EnterPressed
	If over_short_trigger = 0 Then
		CMB_REASON.SelectedIndex = -1
		Sleep(0)
		OpenSpinner(CMB_REASON.cmbBox)
	End If
End Sub

Sub PANEL_BG_TRAIL_Click
	Return True
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		Return True
	End If
End Sub

Sub NOT_UNIT_TRIGGER
	If LABEL_LOAD_SCANCODE.Text = "PCS" And LABEL_PCS_QTY_LOADING.Text = "0" Then
		unit_trigger = 1
	Else If LABEL_LOAD_SCANCODE.Text = "CASE" And LABEL_CASE_QTY_LOADING.Text = "0" Then
		unit_trigger = 1
	Else If LABEL_LOAD_SCANCODE.Text = "BOX" And LABEL_BOX_QTY_LOADING.Text = "0" Then
		unit_trigger = 1
	Else If LABEL_LOAD_SCANCODE.Text = "DOZ" And LABEL_DOZ_QTY_LOADING.Text = "0" Then
		unit_trigger = 1
	Else If LABEL_LOAD_SCANCODE.Text = "BAG" And LABEL_BAG_QTY_LOADING.Text = "0" Then
		unit_trigger = 1
	Else If LABEL_LOAD_SCANCODE.Text = "PACK" And LABEL_PACK_QTY_LOADING.Text = "0" Then
		unit_trigger = 1
	Else If LABEL_LOAD_SCANCODE.Text = "-" Then
		unit_trigger = 0
	Else
		unit_trigger = 0
	End If
End Sub