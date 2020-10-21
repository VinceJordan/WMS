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
	
	'bluetooth
	Dim serial1 As Serial
	Dim AStream As AsyncStreams
	Dim Ts As Timer
	
	Private cartBitmap As Bitmap
	Private dowloadBitmap As Bitmap
	Private uploadBitmap As Bitmap
	
	'string
	Dim principal_id As String
	Dim scan_code As String
	Dim product_id As String
	Dim principal_name As String
	
	'string
	Dim caseper As String
	Dim pcsper As String
	Dim dozper As String
	Dim boxper As String
	Dim bagper As String
	Dim packper As String
	Dim total_pieces As String
	Dim error_trigger As String = 0
	
	Dim EXPDATE, DATENOW As Long
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim CTRL As IME
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
	'bluetooth
	Dim ScannerMacAddress As String
	Dim ScannerOnceConnected As Boolean

	Private XSelections As B4XTableSelections
	Private NameColumn(6) As B4XTableColumn
	
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private DateTemplate As B4XDateTemplate
	Private SearchTemplate2 As B4XSearchTemplate
	
	
	Private cvs As B4XCanvas
	Private xui As XUI
	Private CMB_PRINCIPAL As B4XComboBox
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_MSGBOX1 As Label
	Private LABEL_MSGBOX2 As Label
	Private LABEL_HEADER_TEXT As Label
	Private BUTTON_START As Button
	Private BUTTON_ADD As Button
	Private BUTTON_DELISTED As Button
	Private BUTTON_DOCUMENT As Button
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_DESCRIPTION As Label
	Private TABLE_EXPIRATION_DATE As B4XTable
	Private PANEL_BG_NEW As Panel
	Private EDITTEXT_QTY As EditText
	Private CMB_UNIT As B4XComboBox
	Private LABEL_LOAD_EXPDATE As Label
	Private PANEL_BG_DELISTED As Panel
	Private PANEL_DELISTED As Panel
	Private LISTVIEW_DELISTED As ListView
	Private PANEL_BG_DOCUMENT As Panel
	Private LISTVIEW_DOCUMENT As ListView
	Private LABEL_LOAD_DATE As Label
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
	Activity.LoadLayout("expiration")
	
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	dowloadBitmap = LoadBitmap(File.DirAssets, "download.png")
	uploadBitmap = LoadBitmap(File.DirAssets, "upload.png")
	
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

	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", False)
	End If
	
	Dim p As B4XView = xui.CreatePanel("")
	p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)
	cvs.Initialize(p)
	
	Base = Activity
	Dialog.PutAtTop = True
	Dialog.Initialize (Base)
	Dialog.BorderColor = Colors.Transparent
	Dialog.BorderCornersRadius = 5
	Dialog.TitleBarColor = Colors.RGB(82,169,255)
	Dialog.TitleBarTextColor = Colors.White
	Dialog.BackgroundColor = Colors.White
	Dialog.ButtonsColor = Colors.White
	Dialog.BodyTextColor = Colors.Black
	
	DateTemplate.Initialize
	DateTemplate.MinYear = 2016
	DateTemplate.MaxYear = 2030
	DateTemplate.lblMonth.TextColor = Colors.Black
	DateTemplate.lblYear.TextColor = Colors.Black
	DateTemplate.btnMonthLeft.Color = Colors.RGB(82,169,255)
	DateTemplate.btnMonthRight.Color = Colors.RGB(82,169,255)
	DateTemplate.btnYearLeft.Color = Colors.RGB(82,169,255)
	DateTemplate.btnYearRight.Color = Colors.RGB(82,169,255)
	DateTemplate.DaysInMonthColor = Colors.Black
	
	SearchTemplate2.Initialize
	SearchTemplate2.CustomListView1.DefaultTextBackgroundColor = Colors.White
	SearchTemplate2.CustomListView1.DefaultTextColor = Colors.Black
	SearchTemplate2.SearchField.TextField.TextColor = Colors.Black
	SearchTemplate2.ItemHightlightColor = Colors.White
	SearchTemplate2.TextHighlightColor = Colors.RGB(82,169,255)

	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(82,169,255), 5, 0, Colors.RGB(82,169,255))
	EDITTEXT_QTY.Background = bg
	
	'blueetooth
	serial1.Initialize("Serial")
	Ts.Initialize("Timer", 2000)
	
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	POPULATE_PRINCIPAL
	Sleep(0)
	LOAD_EXPIRATION_HEADER
	Sleep(0)
	GET_PRINCIPAL_ID
	Sleep(0)
	LOG_EXPIRATIONTBL
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "cart", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "download", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("download", dowloadBitmap)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(2, 2, "upload", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("upload", uploadBitmap)
End Sub

Sub Activity_Resume
	Log("Resuming...")
	ShowPairedDevices
	If ScannerOnceConnected=True Then
		Ts.Enabled=True
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
Sub AddBadgeToIcon(bmp As Bitmap, Number1 As Int) As Bitmap
	Dim cvs1 As Canvas
	Dim mbmp As Bitmap
	mbmp.InitializeMutable(32dip, 32dip)
	cvs1.Initialize2(mbmp)
	Dim target As Rect
	target.Initialize(0, 0, mbmp.Width, mbmp.Height)
	cvs1.DrawBitmap(bmp, Null, target)
	If Number1 > 0 Then
		cvs1.DrawCircle(mbmp.Width - 8dip, 8dip, 8dip, Colors.Red, True, 0)
		cvs1.DrawText(Min(Number1, 1000), mbmp.Width - 8dip, 11dip, Typeface.DEFAULT_BOLD, 9, Colors.White, "CENTER")
	End If
	Return mbmp
End Sub
Sub ACToolBarLight1_MenuItemClick (Item As ACMenuItem)
	If Item.Title = "cart" Then
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
	Else if Item.Title = "download" Then
		Msgbox2Async("Are you sure to update your local expiration data?", "Sync Expiration Data", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
		DOWNLOAD_RECEIVING_EXPIRATION
		End If
	Else if Item.Title = "upload" Then
		Msgbox2Async("Are you sure to upload and delist this expiration?", "Sync Delisting Data", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			INSERT_DELISTED
		End If
	End If
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
#Region Scan Barcode
	If principal_id = "" Then
		Msgbox2Async("Please select a principal first.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Else
		Dim trigger As Int = 0
		cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") &"' as INTEGER) or case_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or box_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or bag_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or pack_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER)) and prod_status = '0' and principal_id ='"&principal_id&"' ORDER BY product_id")
		If cursor2.RowCount >= 2 Then
			Dim ls As List
			ls.Initialize
			For row = 0 To cursor2.RowCount - 1
				cursor2.Position = row
				ls.Add(cursor2.GetString("product_desc"))
				DateTime.DateFormat = "yyyy-MM-dd"
			Next
			InputListAsync(ls, "Choose scanner", -1, True) 'show list with paired devices
			Wait For InputList_Result (Result As Int)
			If Result <> DialogResponse.CANCEL Then
				LABEL_LOAD_DESCRIPTION.Text = ls.Get(Result)
				trigger = 0
			Else
				trigger = 1
			End If
					
			'SINGLE SKU
		Else if cursor2.RowCount = 1 Then
			For row = 0 To cursor2.RowCount - 1
				cursor2.Position = row
				LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString("product_desc")
				trigger = 0
			Next
		Else
			cursor4 = connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") &"' as INTEGER) or case_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or box_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or bag_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or pack_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER)) and prod_status = '0' ORDER BY product_id")
			If cursor4.RowCount > 0 Then
				For row = 0 To cursor4.RowCount - 1
					cursor4.Position = row
					cursor7 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & cursor4.GetString("principal_id") & "'")
					If cursor7.RowCount > 0 Then
						For row1 = 0 To cursor7.RowCount - 1
							cursor7.Position = row1
						Next
						Msgbox2Async("The product you scanned :"& CRLF &""&cursor4.GetString("product_desc")&" "& CRLF &"belongs to principal :"& CRLF &""&cursor7.GetString("principal_name")&"", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					End If
				Next
			Else
				Msgbox2Async("The barcode you scanned is not REGISTERED IN THE SYSTEM.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			End If
			trigger = 1
		End If
		If trigger = 0 Then
			scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
			cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
			For qrow = 0 To cursor3.RowCount - 1
				cursor3.Position = qrow
				LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
				principal_id = cursor3.GetString("principal_id")
				product_id = cursor3.GetString("product_id")
			
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
				Sleep(0)
				If scan_code.Trim = cursor3.GetString("case_bar_code") Then
					CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE")
				End If
				If scan_code.Trim = cursor3.GetString("bar_code") Then
					CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS")
				End If
				If scan_code.Trim = cursor3.GetString("box_bar_code") Then
					CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX")
				End If
				If scan_code.Trim = cursor3.GetString("pack_bar_code") Then
					CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK")
				End If
				If scan_code.Trim = cursor3.GetString("bag_bar_code") Then
					CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG")
				End If
	
			
				caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
				pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
				dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
				boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
				bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
				packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
			Next
				LOAD_PRODUCT_EXPIRATION
		End If
	End If
#end Region
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

Sub POPULATE_PRINCIPAL
	CMB_PRINCIPAL.cmbBox.Clear
	CMB_PRINCIPAL.cmbBox.DropdownTextColor = Colors.Black
	CMB_PRINCIPAL.cmbBox.TextColor = Colors.White
	cursor1 = connection.ExecQuery("SELECT principal_name FROM principal_table ORDER BY principal_name ASC")
	For i = 0 To cursor1.RowCount - 1
		Sleep(0)
		cursor1.Position = i
		CMB_PRINCIPAL.cmbBox.Add(cursor1.GetString("principal_name"))
	Next
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
		LOG_EXPIRATIONTBL
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

Sub GET_PRINCIPAL_ID
	If CMB_PRINCIPAL.SelectedIndex <= -1 Then CMB_PRINCIPAL.SelectedIndex = 0
	cursor2 = connection.ExecQuery("SELECT principal_id FROM principal_table WHERE principal_name = '"&CMB_PRINCIPAL.cmbBox.SelectedItem&"'")
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		principal_id = cursor2.GetString("principal_id")
	Next
	Sleep(0)
	INPUT_MANUAL
End Sub

Sub ENABLE_TRANSACTION
	CMB_PRINCIPAL.cmbBox.Enabled = False
	BUTTON_ADD.Enabled = True
	BUTTON_DELISTED.Enabled = True
	BUTTON_DOCUMENT.Enabled = True
	BUTTON_START.Text = " Close"
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.Red, 5, 0, Colors.Red)
	BUTTON_START.Background = bg
End Sub
Sub DISABLE_TRANSACTION
	principal_id = ""
	CMB_PRINCIPAL.cmbBox.Enabled = True
	BUTTON_ADD.Enabled = False
	BUTTON_DELISTED.Enabled = False
	BUTTON_DOCUMENT.Enabled = False
	BUTTON_START.Text = " Start"
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(0,142,255), 5, 0, Colors.Rgb(0,142,255))
	BUTTON_START.Background = bg
	TABLE_EXPIRATION_DATE.Clear
	LABEL_LOAD_DESCRIPTION.Text = "-"
	LABEL_LOAD_VARIANT.Text = "-"
End Sub

Sub LOAD_EXPIRATION_HEADER
	NameColumn(0)=TABLE_EXPIRATION_DATE.AddColumn("Year", TABLE_EXPIRATION_DATE.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_EXPIRATION_DATE.AddColumn("Month", TABLE_EXPIRATION_DATE.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_EXPIRATION_DATE.AddColumn("Date", TABLE_EXPIRATION_DATE.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_EXPIRATION_DATE.AddColumn("Unit", TABLE_EXPIRATION_DATE.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_EXPIRATION_DATE.AddColumn("Quantity", TABLE_EXPIRATION_DATE.COLUMN_TYPE_TEXT)
	NameColumn(5)=TABLE_EXPIRATION_DATE.AddColumn("Days To Expired", TABLE_EXPIRATION_DATE.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_PRODUCT_EXPIRATION
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	Dim date_expired As String
	cursor10 = connection.ExecQuery("SELECT *, sum(quantity) as 'qty', julianday(date_expired) - julianday('now') AS 'days_to_expired' FROM (SELECT a.document_ref_no,a.product_id,a.unit,sum(a.quantity) as 'quantity',a.month_expired,a.year_expired,a.date_expired,b.status FROM product_expiration_ref_table as a " & _
	"LEFT JOIN (SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING') As b " & _
	"ON a.product_id = b.product_id And a.date_expired = b.date_expired And a.unit = b.unit And a.document_ref_no = b.document_ref_no " & _
	"WHERE a.product_id = '"&product_id&"' GROUP BY a.date_expired,b.status,a.unit " & _
	"UNION " & _
	"Select a.document_ref_no,a.product_id,a.unit,sum(a.quantity) As 'quantity',a.month_expired,a.year_expired,a.date_expired,b.status FROM (SELECT *  FROM product_expiration_disc_table WHERE status = 'PENDING') as a " & _
	"LEFT JOIN (SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING') As b " & _
	"ON a.product_id = b.product_id And a.date_expired = b.date_expired And a.unit = b.unit And a.document_ref_no = b.document_ref_no " & _
	"WHERE a.product_id = '"&product_id&"' GROUP BY a.date_expired,b.status,a.unit) GROUP BY date_expired,status,unit")
	If cursor10.RowCount > 0 Then
		For ia = 0 To cursor10.RowCount - 1
			Sleep(10)
			cursor10.Position = ia
			If cursor10.GetString("status") = Null Or cursor10.GetString("status") = "" Then
				date_expired = cursor10.GetString("days_to_expired")			
				Dim row(6) As Object
				row(0) = cursor10.GetString("year_expired")
				row(1) = cursor10.GetString("month_expired")
				row(2) = cursor10.GetString("date_expired")
				row(3) = cursor10.GetString("unit")
				row(4) = cursor10.GetString("qty")
				row(5) = date_expired.SubString2(0,date_expired.IndexOf("."))
				Data.Add(row)
			End If
	Next
		TABLE_EXPIRATION_DATE.RowHeight = 50dip
		Sleep(100)
		ProgressDialogHide
	Else
		ProgressDialogHide
		ToastMessageShow("Data is empty", False)
	End If
	TABLE_EXPIRATION_DATE.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_EXPIRATION_DATE)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_EXPIRATION_DATE_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(1),NameColumn(2), NameColumn(3),NameColumn(4))

		Dim MaxWidth As Int
		Dim MaxHeight As Int
		For i = 0 To TABLE_EXPIRATION_DATE.VisibleRowIds.Size
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
	
	For Each Column As B4XTableColumn In Array (NameColumn(5))

		Dim MaxWidth As Int
		Dim MaxHeight As Int
		For i = 0 To TABLE_EXPIRATION_DATE.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 5dip)
'			lbl.SetTextAlignment(Gravity.RIGHT,Gravity.CENTER)
			MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.Text, lbl.Font).Height + 10dip)
		Next
		
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
		
	Next
	
'	For Each Column As B4XTableColumn In Array (NameColumn(0), NameColumn(1),NameColumn(2))
'
'		For i = 0 To TABLE_EXPIRATION_DATE.VisibleRowIds.Size
'			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
'			Dim lbl As B4XView = pnl.GetView(0)
'			lbl.Font = xui.CreateDefaultBoldFont(18)
'		Next
'	Next
	
	For i = 0 To TABLE_EXPIRATION_DATE.VisibleRowIds.Size - 1
		Dim RowId As Long = TABLE_EXPIRATION_DATE.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(5).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = TABLE_EXPIRATION_DATE.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(5).Id)
			If OtherColumnValue <= 30 Then
				clr = xui.Color_Red
			Else If OtherColumnValue <= 90 Then
				clr = xui.Color_ARGB(255,255,157,0)
			Else If OtherColumnValue <= 150 Then
				clr = xui.Color_Yellow
			End If
			pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Colors.RGB(215,215,215), 0)
			
		End If
	Next
'	For Each Column As B4XTableColumn In Array (NameColumn(0))
'		Column.InternalSortMode= "ASC"
'	Next
	If ShouldRefresh Then
		TABLE_EXPIRATION_DATE.Refresh
		XSelections.Clear
	End If
End Sub
Sub TABLE_EXPIRATION_DATE_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Dim RowData As Map = TABLE_EXPIRATION_DATE.GetRow(RowId)
End Sub
Sub TABLE_EXPIRATION_DATE_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = TABLE_EXPIRATION_DATE.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)
	
	Msgbox2Async("Are you sure you want to delist this product unit expiration", "Delisting Expiration", "YES", "CANCEL", "DELIST THEN ADD", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Or Result = DialogResponse.NEGATIVE Then
		cursor3 = connection.ExecQuery("SELECT DISTINCT document_ref_no FROM product_expiration_ref_table WHERE product_id = '"&product_id&"' and date_expired = '"&RowData.Get("Date")&"' and unit = '"&RowData.get("Unit")&"'")
		If cursor3.RowCount > 0 Then
		For i = 0 To cursor3.RowCount - 1
			Sleep(0)
			cursor3.Position = i
				Dim query As String = "INSERT INTO product_expiration_delisted_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				connection.ExecNonQuery2(query,Array As String(cursor3.GetString("document_ref_no"),principal_id,CMB_PRINCIPAL.cmbBox.SelectedItem,product_id,LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text, _
				RowData.Get("Unit"),RowData.Get("Month"),RowData.Get("Year"),RowData.Get("Date"),DateTime.Date(DateTime.now),DateTime.Time(DateTime.Now),LOGIN_MODULE.tab_id,LOGIN_MODULE.username,"PENDING"))
		Next
	
		End If
		Dim query1 As String = "DELETE FROM product_expiration_disc_table WHERE product_id = ? AND date_expired = ? AND unit = ? AND status = 'PENDING'"
		connection.ExecNonQuery2(query1,Array As String(product_id,RowData.Get("Date"),RowData.Get("Unit")))
		Sleep(0)
		ToastMessageShow("Expiration Delisted",False)
		EDITTEXT_QTY.Text = ""
		LOAD_PRODUCT_EXPIRATION
		If Result = DialogResponse.NEGATIVE Then
			PANEL_BG_NEW.SetVisibleAnimated(300,True)
			PANEL_BG_NEW.BringToFront
			LABEL_LOAD_EXPDATE.Text = RowData.Get("Date")
			Sleep(0)
			OpenSpinner(CMB_UNIT.cmbBox)
			Sleep(0)
		End If
	Else
		
	End If
End Sub

Sub LOG_EXPIRATIONTBL
	cursor5 = connection.ExecQuery("SELECT * FROM database_update_table WHERE database ='Expiration Table'")
	If cursor5.RowCount > 0 Then
		For i = 0 To cursor5.RowCount - 1
			Sleep(0)
			cursor5.Position = i
			ACToolBarLight1.SubTitle = "Last Sync: " & cursor5.GetString("date_time_updated")
		Next
	Else
		
	End If
End Sub

Sub CMB_PRINCIPAL_SelectedIndexChanged (Index As Int)
	GET_PRINCIPAL_ID
	Sleep(0)
	LABEL_LOAD_DESCRIPTION.Text = "-"
	LABEL_LOAD_VARIANT.Text = "-"

	product_id = ""
	LOAD_PRODUCT_EXPIRATION
End Sub

Sub BUTTON_ADD_Click
	If LABEL_LOAD_DESCRIPTION.Text = "-" Then
		
	Else
		PANEL_BG_NEW.SetVisibleAnimated(300,True)
		PANEL_BG_NEW.BringToFront
		Sleep(0)
		Dialog.Title = "Select Expiration Date"
		Wait For (Dialog.ShowTemplate(DateTemplate, "", "", "CANCEL")) Complete (Result As Int)
		If Result = xui.DialogResponse_Positive Then
			LABEL_LOAD_EXPDATE.Text = DateTime.Date(DateTemplate.Date)
			OpenSpinner(CMB_UNIT.cmbBox)
		End If
	End If
End Sub

Sub LABEL_LOAD_EXPDATE_Click
	Dialog.Title = "Select Expiration Date"
	Wait For (Dialog.ShowTemplate(DateTemplate, "", "", "CANCEL")) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_EXPDATE.Text = DateTime.Date(DateTemplate.Date)
		OpenSpinner(CMB_UNIT.cmbBox)
	End If
End Sub

Sub BUTTON_CANCEL_Click
	PANEL_BG_NEW.SetVisibleAnimated(300,False)
	EDITTEXT_QTY.Text = ""
End Sub

Sub GET_REMAINING_DAYS
	DateTime.DateFormat = "yyyy-MM-dd"
	EXPDATE = DateTime.DateParse(LABEL_LOAD_EXPDATE.Text)
	DATENOW = DateTime.DateParse(DateTime.Date(DateTime.Now))
End Sub

Sub DaysBetweenDates(Date1 As Long, Date2 As Long)
	Return Floor((Date2 - Date1) / DateTime.TicksPerDay)
End Sub

Sub BUTTON_CREATE_Click
	If LABEL_LOAD_DESCRIPTION.Text = "-" Then
		ToastMessageShow("Please scan a product first.",False)
	Else
		Dim monthexp As String
		Dim yearexp As String
		GET_REMAINING_DAYS
		Log(DaysBetweenDates (DATENOW,EXPDATE))
		If DaysBetweenDates(DATENOW,EXPDATE) <= 0 Then
			ToastMessageShow("You cannot input a expiration date from to date or back date.", False)
		Else
			If EDITTEXT_QTY.Text = ""  Or EDITTEXT_QTY.Text <= 0 Then
				ToastMessageShow("Please input a quantity higher than zero.", False)
			Else
				If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "01" Then
					monthexp = "January"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "02" Then
					monthexp = "February"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "03" Then
					monthexp = "March"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "04" Then
					monthexp = "April"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "05" Then
					monthexp = "May"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "06" Then
					monthexp = "June"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "07" Then
					monthexp = "July"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "08" Then
					monthexp = "August"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "09" Then
					monthexp = "September"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "10" Then
					monthexp = "October"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "11" Then
					monthexp = "November"
				Else If LABEL_LOAD_EXPDATE.Text.SubString2(5,7) = "12" Then
					monthexp = "December"
				End If
				yearexp = LABEL_LOAD_EXPDATE.Text.SubString2(0,4)
				Sleep(0)
				If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
					total_pieces = caseper * EDITTEXT_QTY.text
				Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
					total_pieces = pcsper * EDITTEXT_QTY.text
				Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
					total_pieces = dozper * EDITTEXT_QTY.text
				Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
					total_pieces = boxper * EDITTEXT_QTY.text
				Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
					total_pieces = bagper * EDITTEXT_QTY.text
				Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
					total_pieces = packper * EDITTEXT_QTY.text
				End If
				Sleep(0)
				Dim transaction_id As String = DateTime.GetYear(DateTime.Now)&DateTime.GetMonth(DateTime.Now)&DateTime.GetDayOfMonth(DateTime.Now)&LOGIN_MODULE.tab_id
				Log(DateTime.GetYear(DateTime.Now)&DateTime.GetMonth(DateTime.Now)&DateTime.GetDayOfMonth(DateTime.Now)&LOGIN_MODULE.tab_id)
				Dim query As String = "INSERT INTO product_expiration_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				connection.ExecNonQuery2(query,Array As String(transaction_id,principal_id,CMB_PRINCIPAL.cmbBox.SelectedItem,product_id,LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text, _
				CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QTY.Text,total_pieces,monthexp,yearexp,LABEL_LOAD_EXPDATE.Text,DateTime.Date(DateTime.now),DateTime.Time(DateTime.Now),LOGIN_MODULE.tab_id,LOGIN_MODULE.username,"PENDING"))
				Sleep(0)
				ToastMessageShow("Product Expiration Added Succesfully",False)
				PANEL_BG_NEW.SetVisibleAnimated(300,False)
				EDITTEXT_QTY.Text = ""
				CTRL.HideKeyboard
				LOAD_PRODUCT_EXPIRATION
			End If
		End If
	End If
End Sub

Sub BUTTON_DELISTED_CLOSE_Click
	PANEL_BG_DELISTED.SetVisibleAnimated(300, False)
	PANEL_BG_DELISTED.BringToFront
End Sub
Sub BUTTON_DELISTED_Click
	If LABEL_LOAD_DESCRIPTION.Text = "-" Then
		ToastMessageShow("Please scan a product first.",False)
	Else
	PANEL_BG_DELISTED.SetVisibleAnimated(300, True)
	PANEL_BG_DELISTED.BringToFront
	Sleep(0)
	LOAD_DELISTED_LIST
	End If
End Sub
Sub LOAD_DELISTED_LIST
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LISTVIEW_DELISTED.Background = bg
	LISTVIEW_DELISTED.Clear
	Sleep(0)
	LISTVIEW_DELISTED.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LISTVIEW_DELISTED.TwoLinesLayout.Label.TextSize = 20
	LISTVIEW_DELISTED.TwoLinesLayout.label.Height = 8%y
	LISTVIEW_DELISTED.TwoLinesLayout.Label.TextColor = Colors.Black
	LISTVIEW_DELISTED.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Top = 6%y
	LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.TextSize = 14
	LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Height = 4%y
	LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LISTVIEW_DELISTED.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LISTVIEW_DELISTED.TwoLinesLayout.ItemHeight = 9%y
	
	cursor2 = connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE product_id = '"&product_id&"' AND status = 'PENDING' GROUP BY date_expired, unit ORDER BY date_expired ASC")
	If cursor2.RowCount > 0 Then
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			LISTVIEW_DELISTED.AddTwoLines2(cursor2.GetString("date_expired"),cursor2.GetString("unit"),cursor2.GetString("date_expired") & "/" & cursor2.GetString("unit"))
		Next
	End If

End Sub
Sub LISTVIEW_DELISTED_ItemClick (Position As Int, Value As Object)

End Sub
Sub LISTVIEW_DELISTED_ItemLongClick (Position As Int, Value As Object)
	Dim date_expired As String = Value
	Dim unit As String = Value
	Msgbox2Async("Are you sure you want to cancel delisting this expiration date", "Cancel Delisted", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Dim query As String = "DELETE from product_expiration_delisted_table WHERE product_id = ? AND date_expired = ? AND unit = ? AND status = 'PENDING'"
		connection.ExecNonQuery2(query,Array As String(product_id,date_expired.SubString2(0,date_expired.IndexOf("/")),unit.SubString2(unit.IndexOf("/")+1,unit.Length)))
		Log(date_expired.SubString2(0,date_expired.IndexOf("/")))
		Log(unit.SubString2(unit.IndexOf("/")+1,unit.Length))
		Sleep(0)
		LOAD_DELISTED_LIST
		Sleep(0)
		LOAD_PRODUCT_EXPIRATION
	End If
End Sub

Sub BUTTON_DOCUMENT_Click
	If LABEL_LOAD_DESCRIPTION.Text = "-" Then
	ToastMessageShow("Please scan a product first.",False)
	Else
	PANEL_BG_DOCUMENT.SetVisibleAnimated(300, True)
	PANEL_BG_DOCUMENT.BringToFront
	Sleep(0)
	LOAD_DOCUMENT_LIST
	End If
End Sub
Sub BUTTON_DOCUMENT_CLOSE_Click
	PANEL_BG_DOCUMENT.SetVisibleAnimated(300, False)
	PANEL_BG_DOCUMENT.BringToFront
End Sub
Sub LOAD_DOCUMENT_LIST
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LISTVIEW_DOCUMENT.Background = bg
	LISTVIEW_DOCUMENT.Clear
	Sleep(0)
	LISTVIEW_DOCUMENT.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LISTVIEW_DOCUMENT.TwoLinesLayout.Label.TextSize = 20
	LISTVIEW_DOCUMENT.TwoLinesLayout.label.Height = 8%y
	LISTVIEW_DOCUMENT.TwoLinesLayout.Label.TextColor = Colors.Black
	LISTVIEW_DOCUMENT.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Top = 6%y
	LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.TextSize = 14
	LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Height = 4%y
	LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LISTVIEW_DOCUMENT.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LISTVIEW_DOCUMENT.TwoLinesLayout.ItemHeight = 9%y
	
	cursor3 = connection.ExecQuery("SELECT * FROM (SELECT document_ref_no, unit, sum(quantity) as 'quantity', date_expired, legend, date_registered FROM product_expiration_ref_table WHERE product_id = '"&product_id&"' GROUP BY date_expired,document_ref_no, unit " & _
	"UNION " & _
	"SELECT document_ref_no, unit, sum(quantity) as 'quantity', date_expired, 'ADDED IN TAB' as 'legend', date_registered FROM product_expiration_disc_table WHERE product_id = '"&product_id&"' and status = 'PENDING' GROUP BY date_expired,document_ref_no, unit) GROUP BY date_expired,document_ref_no,unit ORDER BY date_registered")
	If cursor3.RowCount > 0 Then
		For row = 0 To cursor3.RowCount - 1
			cursor3.Position = row
			LISTVIEW_DOCUMENT.AddTwoLines2(cursor3.GetString("date_expired")& " | " &cursor3.GetString("quantity")& " " &cursor3.GetString("unit"), cursor3.GetString("document_ref_no") & " - " & cursor3.GetString("legend") & " (" & cursor3.GetString("date_registered")& ")", _
			cursor3.GetString("date_expired")& " | " &cursor3.GetString("unit") & " ! " & cursor3.GetString("document_ref_no") & " / " & cursor3.GetString("legend"))
		Next
	End If

End Sub

Sub INSERT_DELISTED
	error_trigger = 0
	PANEL_BG_MSGBOX.BringToFront
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
	LABEL_MSGBOX2.Text = "Fetching data..."
	cursor1 = connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING' ORDER by product_variant, product_description")
	If cursor1.RowCount > 0 Then
		For i3 = 0 To cursor1.RowCount - 1
			cursor1.Position = i3
			Dim cmd As DBCommand = CreateCommand("insert_expiration_delisted", Array As String(cursor1.GetString("document_ref_no"), cursor1.GetString("principal_id") _
			,cursor1.GetString("principal_name"),cursor1.GetString("product_id"),cursor1.GetString("product_variant"),cursor1.GetString("product_description"),cursor1.GetString("unit") _
			,cursor1.GetString("month_expired"),cursor1.GetString("year_expired"),cursor1.GetString("date_expired"),cursor1.GetString("date_delisted"),cursor1.GetString("time_delisted") _
			,cursor1.GetString("tab_id"),cursor1.GetString("user_info")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Uploading :" & cursor1.GetString("product_description")
			Else
				error_trigger = 1
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next
	End If
	If error_trigger = 0 Then
		UPDATE_RECEIVING_DELISTING
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			INSERT_DELISTED
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub UPDATE_RECEIVING_DELISTING
	cursor2 = connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING' ORDER by product_variant, product_description")
	If cursor2.RowCount > 0 Then
		For i3 = 0 To cursor2.RowCount - 1
			cursor2.Position = i3
			Dim cmd As DBCommand = CreateCommand("update_expiration_receiving_delisted_old", Array As String("DELISTED", cursor2.GetString("document_ref_no"), cursor2.GetString("product_id"), _
			cursor2.GetString("date_expired"),cursor2.GetString("unit")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Delisting :" & cursor2.GetString("product_description")
				Dim query As String = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?"
				connection.ExecNonQuery2(query,Array As String(cursor2.GetString("document_ref_no"), cursor2.GetString("product_id"), cursor2.GetString("date_expired"), cursor2.GetString("unit")))
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("update_expiration_receiving_delisted", Array As String("DELISTED", cursor2.GetString("document_ref_no"), cursor2.GetString("product_id"), _
			cursor2.GetString("date_expired"),cursor2.GetString("unit")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Delisting :" & cursor2.GetString("product_description")
				Dim query As String = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?"
				connection.ExecNonQuery2(query,Array As String(cursor2.GetString("document_ref_no"), cursor2.GetString("product_id"), cursor2.GetString("date_expired"), cursor2.GetString("unit")))
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("update_expiration_audit_delisted", Array As String("DELISTED", cursor2.GetString("document_ref_no"), cursor2.GetString("product_id"), _
			cursor2.GetString("date_expired"),cursor2.GetString("unit")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Delisting :" & cursor2.GetString("product_description")
				Dim query As String = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?"
				connection.ExecNonQuery2(query,Array As String(cursor2.GetString("document_ref_no"), cursor2.GetString("product_id"), cursor2.GetString("date_expired"), cursor2.GetString("unit")))
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next
		If error_trigger = 0 Then
			INSERT_EXPIRATION
		Else
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				INSERT_DELISTED
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
	Else
		UPDATE_AUDIT_DELISTING
	End If
End Sub
Sub UPDATE_AUDIT_DELISTING
	cursor5 = connection.ExecQuery("SELECT * FROM product_expiration_delisted_table WHERE status = 'PENDING' ORDER by product_variant, product_description")
	If cursor5.RowCount > 0 Then
		For i3 = 0 To cursor3.RowCount - 1
			cursor5.Position = i3
			Dim cmd As DBCommand = CreateCommand("update_expiration_audit_delisted", Array As String("DELISTED", cursor5.GetString("document_ref_no"), cursor5.GetString("product_id"), _
			cursor5.GetString("date_expired"),cursor5.GetString("unit")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Delisting :" & cursor5.GetString("product_description")
				Dim query As String = "UPDATE product_expiration_delisted_table SET status = 'DELISTED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?"
				connection.ExecNonQuery2(query,Array As String(cursor5.GetString("document_ref_no"), cursor5.GetString("product_id"), cursor5.GetString("date_expired"), cursor5.GetString("unit")))
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next

	End If
	If error_trigger = 0 Then
		INSERT_EXPIRATION
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			INSERT_DELISTED
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub INSERT_EXPIRATION
	cursor4 = connection.ExecQuery("SELECT * FROM product_expiration_disc_table WHERE status = 'PENDING' ORDER by product_variant, product_description")
	If cursor4.RowCount > 0 Then
		For i3 = 0 To cursor4.RowCount - 1
			cursor4.Position = i3
			Dim cmd As DBCommand = CreateCommand("insert_expiration", Array As String(cursor4.GetString("document_ref_no"), cursor4.GetString("principal_id") _
			,cursor4.GetString("principal_name"),cursor4.GetString("product_id"),cursor4.GetString("product_variant"),cursor4.GetString("product_description"),cursor4.GetString("unit"),cursor4.GetString("quantity") _
			,cursor4.GetString("total_pieces"),cursor4.GetString("month_expired"),cursor4.GetString("year_expired"),cursor4.GetString("date_expired"),cursor4.GetString("date_registered"),cursor4.GetString("time_registered") _
			,cursor4.GetString("tab_id"),cursor4.GetString("user_info"),"EXISTING"))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				LABEL_MSGBOX2.Text = "Uploading :" & cursor4.GetString("product_description")
				Dim query As String = "UPDATE product_expiration_disc_table SET status = 'UPLOADED' WHERE document_ref_no = ? AND product_id = ? AND date_expired = ? AND unit = ?"
				connection.ExecNonQuery2(query,Array As String(cursor4.GetString("document_ref_no"), cursor4.GetString("product_id"), cursor4.GetString("date_expired"), cursor4.GetString("unit")))
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Sleep(1000)
			End If
			js.Release
		Next

	End If
	If error_trigger = 0 Then
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Sucessfull..", False)
		DOWNLOAD_RECEIVING_EXPIRATION
		Sleep(0)
		LABEL_LOAD_DESCRIPTION.Text = "-"
		LABEL_LOAD_VARIANT.Text = "-"
		product_id = ""
		LOAD_PRODUCT_EXPIRATION
	Else
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			INSERT_DELISTED
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub

Sub BUTTON_MANUAL_Click
	Dim ls As List
	ls.Initialize
	ls.Add("BARCODE NOT REGISTERED")
	ls.Add("NO ACTUAL BARCODE")
	ls.Add("NO SCANNER")
	ls.Add("DAMAGE BARCODE")
	ls.Add("SCANNER CAN'T READ BARCODE")
	InputListAsync(ls, "Choose reason", -1, True) 'show list with paired devices
	Wait For InputList_Result (Result2 As Int)
	If Result2 <> DialogResponse.CANCEL Then
		Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate2, "", "", "CANCEL")
		Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
		Wait For (rs) Complete (Result As Int)
		If Result = xui.DialogResponse_Positive Then
			cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&SearchTemplate2.SelectedItem&"'")
			For qrow = 0 To cursor3.RowCount - 1
				cursor3.Position = qrow
				LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
				LABEL_LOAD_DESCRIPTION.Text = cursor3.GetString("product_desc")
				principal_id = cursor3.GetString("principal_id")
				product_id = cursor3.GetString("product_id")
			
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
			
				caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
				pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
				dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
				boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
				bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
				packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
			Next
			CTRL.HideKeyboard
			LOAD_PRODUCT_EXPIRATION
		End If
	End If
End Sub
Sub INPUT_MANUAL
	SearchTemplate2.CustomListView1.Clear
	Dialog.Title = "Find Product"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor7 = connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' and principal_id = '"&principal_id&"' ORDER BY product_desc ASC")
	For i = 0 To cursor7.RowCount - 1
		Sleep(0)
		cursor7.Position = i
		Items.Add(cursor7.GetString("product_desc"))
	Next
	SearchTemplate2.SetItems(Items)
End Sub

Sub PANEL_BG_MSGBOX_Click
	Return True
End Sub
Sub PANEL_BG_NEW_Click
	Return True
End Sub
Sub PANEL_BG_DELISTED_Click
	Return True
End Sub
Sub PANEL_BG_DOCUMENT_Click
	Return True
End Sub

