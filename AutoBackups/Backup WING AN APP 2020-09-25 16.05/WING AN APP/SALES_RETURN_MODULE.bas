B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=10
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
	
	Dim cartBitmap As Bitmap
	Dim addBitmap As Bitmap
	Dim uploadBitmap As Bitmap
	
	'string
	Dim return_id As String
	Dim return_number As String
	Dim customer_id As String
	Dim plate_no As String
	Dim sales_position_id As String
	Dim error_trigger As String
	
	'Product
	Dim principal_id As String
	Dim product_id As String
	Dim reason As String
	Dim caseper As String
	Dim pcsper As String
	Dim dozper As String
	Dim boxper As String
	Dim bagper As String
	Dim packper As String
	Dim total_pieces As String
	Dim scan_code As String
	Dim transaction_number As String
	Dim cancelled_trigger As String
	Dim cancelled_id As String
	Dim total_invoice As String
	Dim total_cancelled As String
	
	'WRONG
	'Product
	Dim wrong_principal_id As String
	Dim wrong_product_id As String
	Dim wrong_reason As String
	Dim wrong_caseper As String
	Dim wrong_pcsper As String
	Dim wrong_dozper As String
	Dim wrong_boxper As String
	Dim wrong_bagper As String
	Dim wrong_packper As String
	Dim wrong_total_pieces As String
	Dim wrong_scan_code As String
	Dim wrong_trigger As String = 0
	
	
	Dim item_number As String
	
	'Exp
	Dim lifespan_month As String
	Dim lifespan_year As String
	Dim default_reading As String
	Dim monthexp As String
	Dim yearexp As String
	Dim monthmfg As String
	Dim yearmfg As String
	
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	'ime
	Dim CTRL As IME
	
	'bluetooth
	Dim ScannerMacAddress As String
	Dim ScannerOnceConnected As Boolean
	
	Private XSelections As B4XTableSelections
	Private NameColumn(8) As B4XTableColumn
	
	
	Private cvs As B4XCanvas
	Private xui As XUI
	
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private DateTemplate As B4XDateTemplate
	Private DateTemplate2 As B4XDateTemplate
	Private SearchTemplate As B4XSearchTemplate
	Private SearchTemplate2 As B4XSearchTemplate
	Private InputTemplate As B4XInputTemplate
		
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	Private LABEL_LOAD_CUSTOMER_NAME As Label
	Private LABEL_LOAD_RETURN_DATE As Label
	Private LABEL_LOAD_RETURN_ID As Label
	Private CMB_SALESMAN As B4XComboBox
	Private CMB_WAREHOUSE As B4XComboBox
	Private CMB_REASON As B4XComboBox
	Private EDITTEXT_QUANTITY As EditText
	Private CMB_UNIT As B4XComboBox
	Private LABEL_LOAD_DESCRIPTION As Label
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_PRINCIPAL As Label
	Private BUTTON_EXIT As Button
	Private PANEL_BG_INPUT As Panel
	Private BUTTON_SAVE As Button
	Private TABLE_SALES_RETURN As B4XTable
	Private BUTTON_CANCEL As Button
	Private LVL_RID As ListView
	Private PANEL_BG_RID As Panel
	Private BUTTON_MANUAL As Button
	Private BUTTON_UPLOAD As Button
	Private LABEL_MSGBOX2 As Label
	Private LABEL_MSGBOX1 As Label
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_HEADER_LOGO As Label
	Private LABEL_HEADER_TEXT As Label
	Private PANEL_BG_EXPIRATION As Panel
	Private LABEL_LOAD_EXPIRATION As Label
	Private LABEL_LOAD_MANUFACTURED As Label
	Private BUTTON_EXPIRATION As Button
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
	Activity.LoadLayout("sales_return")

	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	addBitmap = LoadBitmap(File.DirAssets, "add.png")
	uploadBitmap = LoadBitmap(File.DirAssets, "invoice.png")
	
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

	ACToolBarLight1.Elevation = 1dip

	'blueetooth
	serial1.Initialize("Serial")
	Ts.Initialize("Timer", 2000)

	Dim p As B4XView = xui.CreatePanel("")
	p.SetLayoutAnimated(0, 10%x, 20%y, 80%y, 40%y)
	cvs.Initialize(p)
#Region Dialog	
	Base = Activity
	Dialog.Initialize(Base)
	Dialog.BorderColor = Colors.Transparent
	Dialog.BorderCornersRadius = 5
	Dialog.TitleBarColor = Colors.RGB(82,169,255)
	Dialog.TitleBarTextColor = Colors.White
	Dialog.BackgroundColor = Colors.White
	Dialog.ButtonsColor = Colors.White
	Dialog.ButtonsTextColor = Colors.Black
	
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
	
	DateTemplate2.Initialize
	DateTemplate2.MinYear = 2016
	DateTemplate2.MaxYear = 2030
	DateTemplate2.lblMonth.TextColor = Colors.Black
	DateTemplate2.lblYear.TextColor = Colors.Black
	DateTemplate2.btnMonthLeft.Color = Colors.RGB(82,169,255)
	DateTemplate2.btnMonthRight.Color = Colors.RGB(82,169,255)
	DateTemplate2.btnYearLeft.Color = Colors.RGB(82,169,255)
	DateTemplate2.btnYearRight.Color = Colors.RGB(82,169,255)
	DateTemplate2.DaysInMonthColor = Colors.Black

	SearchTemplate.Initialize
	SearchTemplate.CustomListView1.DefaultTextBackgroundColor = Colors.White
	SearchTemplate.CustomListView1.DefaultTextColor = Colors.Black
	SearchTemplate.SearchField.TextField.TextColor = Colors.Black
	SearchTemplate.ItemHightlightColor = Colors.White
	SearchTemplate.TextHighlightColor = Colors.RGB(82,169,255)
	
	SearchTemplate2.Initialize
	SearchTemplate2.CustomListView1.DefaultTextBackgroundColor = Colors.White
	SearchTemplate2.CustomListView1.DefaultTextColor = Colors.Black
	SearchTemplate2.SearchField.TextField.TextColor = Colors.Black
	SearchTemplate2.ItemHightlightColor = Colors.White
	SearchTemplate2.TextHighlightColor = Colors.RGB(82,169,255)
	
	InputTemplate.Initialize
	InputTemplate.SetBorderColor(Colors.Green,Colors.RGB(215,215,215))
	InputTemplate.TextField1.TextColor = Colors.Black
	
#End Region

	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
'	EDITTEXT_TYPE.Background = bg
	
	
'	Sleep(0)
'	Dim Ref As Reflector
'	Ref.Target = EDITTEXT_QUANTITY ' The text field being referenced
'	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
	EDITTEXT_QUANTITY.Background = bg
	Sleep(0)
	RETURN_MANUAL
	Sleep(0)
	LOAD_WAREHOUSE
	Sleep(0)
	GET_CUSTOMER
	Sleep(0)
	LOAD_RETURN_PRODUCT_HEADER
	BUTTON_MANUAL.Visible = False
	BUTTON_UPLOAD.Visible = False
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "See Returns ID", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("See Returns ID", uploadBitmap)
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(2, 2, "cart", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "Add New Transaction", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Add New Transaction", addBitmap)
	Sleep(0)
End Sub

Sub Activity_Resume
'	ShowPairedDevices
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

Sub OpenLabel(se As Label)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
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
	StartActivity(RETURN_MODULE)
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
	Else if Item.Title = "See Returns ID" Then
		LOAD_RETURN_UPLOAD
	Else if Item.Title = "Add New Transaction" Then
		If LABEL_LOAD_RETURN_ID.Text <> "-" Then
		Msgbox2Async("Is this new transaction is still the existing customer?" ,"Creating New Transaction", "New Customer", "", "Existing Customer", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			SHOW_CUSTOMER
			Sleep(0)
			LOAD_RETURN_PRODUCT
		Else if Result = DialogResponse.NEGATIVE Then
			LABEL_LOAD_CUSTOMER_NAME.Text = LABEL_LOAD_CUSTOMER_NAME.Text 
			NEW_TRANSACTION
			Sleep(0)
			LOAD_RETURN_PRODUCT
		End If
		Else
			SHOW_CUSTOMER
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
'	LABEL_LOAD_BARCODE.Text = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	Dim trigger As Int = 0

#Region Scan Barcode
	If LABEL_LOAD_RETURN_ID.Text <> "-" Then
		cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") &"' as INTEGER) or case_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or box_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or bag_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or pack_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER)) and prod_status = '0' ORDER BY product_id")
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
				Log(1)
				LABEL_LOAD_DESCRIPTION.Text = cursor2.GetString("product_desc")
				trigger = 0
			Next
		Else
			PANEL_BG_INPUT.SetVisibleAnimated(300,False)
			Msgbox2Async("The barcode you scanned is not REGISTERED IN THE SYSTEM.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			trigger = 1
'		CLEAR_INPUT
		End If
		Sleep(0)
		If trigger = 0 Then
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
			
				caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
				pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
				dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
				boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
				bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
				packper = cursor3.GetString("PACK_UNIT_PER_PCS")
			
				default_reading = cursor3.GetString("default_expiration_date_reading")
				lifespan_year = cursor3.GetString("life_span_year")
				lifespan_month = cursor3.GetString("life_span_month")
		
			Next
			cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
			If cursor6.RowCount > 0 Then
				For row = 0 To cursor6.RowCount - 1
					cursor6.Position = row
					LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring("principal_name")
				Next
			End If
			PANEL_BG_INPUT.SetVisibleAnimated(300,True)		
			Sleep(0)
			EDITTEXT_QUANTITY.Text = ""
			CMB_WAREHOUSE.SelectedIndex = -1
			Sleep(0)
			OpenSpinner(CMB_WAREHOUSE.cmbBox)
			reason = "N/A"
			scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
			CMB_REASON.cmbBox.Clear
			Sleep(0)
			CLEAR_EXPIRATION
		End If
#End Region
	Else
		Msgbox2Async("SELECT or CREATE a transaction first before scannning.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)		
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
Sub GET_CUSTOMER
	ProgressDialogShow2("Getting Customer...", False)
	SearchTemplate2.CustomListView1.Clear
	cursor3 = connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE return_route_id = '"&RETURN_MODULE.return_route_id&"'")
	If cursor3.RowCount > 0 Then
		connection.ExecNonQuery("DELETE FROM customer_table")
		Sleep(0)
		For isss = 0 To cursor3.RowCount - 1
			Sleep(0)
			cursor3.Position = isss
			Dim req1 As DBRequestManager = CreateRequest
			Dim cmd1 As DBCommand = CreateCommand("select_return_customer", Array As String(cursor3.GetString("picklist_id")))
			Wait For (req1.ExecuteQuery(cmd1, 0, Null)) JobDone(js As HttpJob)
			If js.Success Then
					
				req1.HandleJobAsync(js, "req1")
				Wait For (req1) req1_Result(res1 As DBResult)
				If res1.Rows.Size > 0 Then
					For Each row1() As Object In res1.Rows
						Dim query As String = "INSERT INTO customer_table VALUES (?,?)"
						connection.ExecNonQuery2(query,Array As String(row1(res1.Columns.Get("customer_id")),row1(res1.Columns.Get("account_customer_name"))))
						Sleep(0)
						ToastMessageShow("Customer Acquired", False)
					Next
					LOAD_CUSTOMER
				Else
					Msgbox2Async("This picklist have no exisiting customer", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				End If
			Else
				ProgressDialogHide
				Log("ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Updating Error", False)
			End If
			js.Release
		Next
		ProgressDialogHide
		Sleep(100)
		GET_SALESMAN
	Else
		ProgressDialogHide
		Msgbox2Async("Picklist Route is empty, Please advice IT for this conflict.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
End Sub
Sub GET_SALESMAN
	ProgressDialogShow2("Getting Salesman...", False)
	cursor9 = connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE  return_route_id = '"&RETURN_MODULE.return_route_id&"'")
	If cursor9.RowCount > 0 Then
		connection.ExecNonQuery("DELETE FROM salesman_table")
		Sleep(0)
		For iaaa = 0 To cursor9.RowCount - 1
			Sleep(0)
			cursor9.Position = iaaa
		
			Dim req1 As DBRequestManager = CreateRequest
			Dim cmd1 As DBCommand = CreateCommand("select_salesman", Array As String(cursor9.GetString("picklist_id")))
			Wait For (req1.ExecuteQuery(cmd1, 0, Null)) JobDone(js As HttpJob)
			If js.Success Then
					
				req1.HandleJobAsync(js, "req1")
				Wait For (req1) req1_Result(res1 As DBResult)
				If res1.Rows.Size > 0 Then
					For Each row1() As Object In res1.Rows
						Dim query As String = "INSERT INTO salesman_table VALUES (?,?,?,?)"
						connection.ExecNonQuery2(query,Array As String(row1(res1.Columns.Get("employee_id")),row1(res1.Columns.Get("sales_position_id")) _
						,row1(res1.Columns.Get("sales_position_name")),row1(res1.Columns.Get("last_name")) & " " & row1(res1.Columns.Get("first_name"))))
						Sleep(0)
						ToastMessageShow("Salesman Acquired", False)
						Log(row1(res1.Columns.Get("sales_position_name"))&"("&row1(res1.Columns.Get("last_name"))& " " &row1(res1.Columns.Get("first_name"))&")")
					Next
				Else
					Msgbox2Async("This picklist have no exisiting customer", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				End If
			Else
				ProgressDialogHide
				Log("ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Updating Error", False)
			End If
			js.Release
		Next
		ProgressDialogHide
		LOAD_SALESMAN
		Sleep(100)
		cursor7 = connection.ExecQuery("SELECT * FROM sales_return_ref_table WHERE return_route_id = '"&RETURN_MODULE.return_route_id&"'")
		If cursor7.RowCount > 0 Then
				Msgbox2Async("Do you want to add a new transaction?","Option", "NEW", "CANCEL", "SEARCH EXISTING", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					SHOW_CUSTOMER
				Else If Result = DialogResponse.NEGATIVE Then
					LOAD_RETURN_UPLOAD
				End If
		Else
			SHOW_CUSTOMER
		End If
	Else
		ProgressDialogHide
		Msgbox2Async("Picklist Route is empty, Please advice IT for this conflict.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
End Sub
#End Region

Sub SHOW_CUSTOMER
	ProgressDialogShow2("Loading...", False)
	Sleep(2000)
	Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate2, "", "", "CANCEL")
	Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
	ProgressDialogHide()
'	If LABEL_LOAD_CUSTOMER_NAME.Text <> "-" Then
'		SearchTemplate2.SearchField.Text = LABEL_LOAD_CUSTOMER_NAME.Text
'		Sleep(0)
'		SearchTemplate2.SearchField.RequestFocusAndShowKeyboard
'	Else
	SearchTemplate2.SearchField.Text = ""
	Sleep(0)
	SearchTemplate2.SearchField.RequestFocusAndShowKeyboard
'	End If
	Wait For (rs) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_CUSTOMER_NAME.Text = SearchTemplate2.SelectedItem
		NEW_TRANSACTION
	End If

		
End Sub
Sub LOAD_CUSTOMER
	SearchTemplate2.CustomListView1.Clear
	Dialog.Title = "Please Choose Customer"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor10 = connection.ExecQuery("SELECT * FROM customer_table GROUP BY customer_id")
	For iss = 0 To cursor10.RowCount - 1
		cursor10.Position = iss
		Sleep(100)
		Log(cursor10.GetString("customer_name"))
		Items.Add(cursor10.GetString("customer_name"))
	Next
	SearchTemplate2.SetItems(Items)
End Sub

Sub GET_RETURN_NUMBER
	cursor6 = connection.ExecQuery("SELECT MAX(CAST(return_number as INT)) as 'return_number' from sales_return_ref_table")
	For i = 0 To cursor6.RowCount - 1
		cursor6.Position = i
		If cursor6.GetString("return_number") = Null Or cursor6.GetString("return_number") = "" Then
			return_number =1
		Else
			return_number = cursor6.GetString("return_number") + 1
		End If
	Next
End Sub
Sub GET_CUSTOMER_ID
	cursor4 = connection.ExecQuery("SELECT * FROM customer_table WHERE customer_name = '"&LABEL_LOAD_CUSTOMER_NAME.Text&"'")
	For i = 0 To cursor4.RowCount - 1
		cursor4.Position = i
		customer_id = cursor4.GetString("customer_id")
	Next
End Sub
Sub NEW_TRANSACTION
	GET_RETURN_NUMBER
	Sleep(0)
	Dim CExpDate As String = DateTime.GetYear(DateTime.Now)
	Dim Cxmo As String = DateTime.GetMonth(DateTime.Now)
	Dim Cxday As String = DateTime.GetDayOfMonth(DateTime.Now)
	Dim Cxtime As String = DateTime.GetHour(DateTime.Now)
	Dim Cxmin As String = DateTime.GetMinute(DateTime.Now)
	Dim Cxsecs As String = DateTime.GetSecond(DateTime.Now)
	Sleep(0)
	return_id = CExpDate.SubString(2)&Cxmo&Cxday&Cxtime&"-"&return_number&LOGIN_MODULE.tab_id
	Log(return_id)
	LABEL_LOAD_RETURN_DATE.Text = DateTime.Date(DateTime.Now)
	LABEL_LOAD_RETURN_ID.Text = return_id
	Sleep(0)
	GET_CUSTOMER_ID
	Sleep(0)
	Dim query As String = "INSERT INTO sales_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(RETURN_MODULE.return_route_id,return_id,LABEL_LOAD_RETURN_DATE.Text,return_number,customer_id,LABEL_LOAD_CUSTOMER_NAME.Text, _
	DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),LOGIN_MODULE.username,LOGIN_MODULE.tab_id))
	Sleep(0)
	ToastMessageShow("Transaction Created",False)
	BUTTON_MANUAL.Visible = True
	BUTTON_UPLOAD.Visible = True
End Sub

Sub LOAD_SALESMAN
	CMB_SALESMAN.cmbBox.Clear
	cursor8 = connection.ExecQuery("SELECT * FROM salesman_table GROUP BY employee_id")
	For iqq = 0 To cursor8.RowCount - 1
		cursor8.Position = iqq
		CMB_SALESMAN.cmbBox.Add(cursor8.GetString("sales_position_name")&"("&cursor8.GetString("employee_name")&")")
	Next
End Sub
Sub LOAD_WAREHOUSE
	CMB_WAREHOUSE.cmbBox.Clear
	CMB_WAREHOUSE.cmbBox.add("BO WAREHOUSE")
	CMB_WAREHOUSE.cmbBox.add("MAIN WAREHOUSE")
End Sub
Sub BUTTON_EXIT_Click
	CLEAR
	Sleep(0)
	LOAD_RETURN_PRODUCT
End Sub
Sub BUTTON_CANCEL_Click
	CLEAR
	Sleep(0)
	LOAD_RETURN_PRODUCT
End Sub
Sub LOAD_REASON
	If CMB_WAREHOUSE.SelectedIndex = CMB_WAREHOUSE.cmbBox.IndexOf("BO WAREHOUSE") Then
		CMB_REASON.cmbBox.Clear
		CMB_REASON.cmbBox.Add("DAMAGED")
		CMB_REASON.cmbBox.Add("EXPIRED")
		CMB_REASON.cmbBox.Add("NEAR EXPIRY")
		CMB_REASON.cmbBox.Add("FACTORY DEFECT")
		CMB_REASON.cmbBox.Add("DENTED")
		CMB_REASON.cmbBox.Add("EMPTY")
		CMB_REASON.cmbBox.Add("INSECT BITE")
		CMB_REASON.cmbBox.Add("NO RETURN")
	Else
		CMB_REASON.cmbBox.Clear
		CMB_REASON.cmbBox.Add("NEAR EXPIRY")
		CMB_REASON.cmbBox.Add("SLOW MOVING")
		CMB_REASON.cmbBox.Add("OVER STOCK")
		CMB_REASON.cmbBox.Add("NO CASH/CHEQUE")
		CMB_REASON.cmbBox.Add("BANKRUPTCY")
		CMB_REASON.cmbBox.Add("NO RETURN")
	End If
End Sub
Sub CMB_WAREHOUSE_SelectedIndexChanged (Index As Int)
	LOAD_REASON
	Sleep(0)
	CMB_REASON.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_REASON.cmbBox)
End Sub
Sub CMB_REASON_SelectedIndexChanged (Index As Int)
	If BUTTON_SAVE.Text = " SAVE" Then
		If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_WAREHOUSE.cmbBox.IndexOf("MAIN WAREHOUSE") Then
			PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)
			PANEL_BG_EXPIRATION.BringToFront
			CLEAR_EXPIRATION
			If default_reading = "BOTH" Or default_reading = "Expiration Date" Then
				OpenLabel(LABEL_LOAD_EXPIRATION)
			Else If default_reading = "Manufacturing Date" Then
				OpenLabel(LABEL_LOAD_MANUFACTURED)
			Else
		
			End If
		Else
			If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("NEAR EXPIRY") Then
				PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)
				PANEL_BG_EXPIRATION.BringToFront
				CLEAR_EXPIRATION
				If default_reading = "BOTH" Or default_reading = "Expiration Date" Then
					OpenLabel(LABEL_LOAD_EXPIRATION)
				Else If default_reading = "Manufacturing Date" Then
					OpenLabel(LABEL_LOAD_MANUFACTURED)
				Else
		
				End If
			Else
				CMB_UNIT.SelectedIndex = -1
				Sleep(0)
				OpenSpinner(CMB_UNIT.cmbBox)
				CLEAR_EXPIRATION
			End If
		End If
	Else
		If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_WAREHOUSE.cmbBox.IndexOf("MAIN WAREHOUSE") Then
			PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)
			PANEL_BG_EXPIRATION.BringToFront
			LOAD_EXPIRATION
		Else
			If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("NEAR EXPIRY") Then
				PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)
				PANEL_BG_EXPIRATION.BringToFront
				LOAD_EXPIRATION
			Else
				CLEAR_EXPIRATION
			End If
		End If
	End If
End Sub
Sub CMB_UNIT_SelectedIndexChanged (Index As Int)
	EDITTEXT_QUANTITY.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
	EDITTEXT_QUANTITY.SelectAll
End Sub
Sub GET_SALESMAN_ID
	cursor4 = connection.ExecQuery("SELECT * FROM salesman_table WHERE sales_position_name = '"&CMB_SALESMAN.cmbBox.SelectedItem.SubString2(0,CMB_SALESMAN.cmbBox.SelectedItem.IndexOf("("))&"'")
	For i = 0 To cursor4.RowCount - 1
		cursor4.Position = i
		sales_position_id = cursor4.GetString("sales_position_id")
		Log(sales_position_id)
	Next
End Sub
Sub BUTTON_SAVE_Click
	If LOGIN_MODULE.username <> "" Or LOGIN_MODULE.tab_id <> "" Then
		If EDITTEXT_QUANTITY.Text = "" Or EDITTEXT_QUANTITY.Text <= 0  Then
			Msgbox2Async("You cannot input a zero value quantity.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			EDITTEXT_QUANTITY.RequestFocus
			Sleep(0)
			CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
			Sleep(0)
			EDITTEXT_QUANTITY.SelectAll
		Else
			If CMB_UNIT.cmbBox.SelectedIndex = -1 Then
				CMB_UNIT.cmbBox.SelectedIndex = 0
			End If
					
			If CMB_REASON.cmbBox.SelectedIndex = -1 Then
				CMB_REASON.cmbBox.SelectedIndex = 0
			End If
			
			If CMB_WAREHOUSE.cmbBox.SelectedIndex = -1 Then
				CMB_WAREHOUSE.cmbBox.SelectedIndex = 0
			End If
			Sleep(0)
			Log(2)
			If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
				total_pieces = caseper * EDITTEXT_QUANTITY.text
			Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
				total_pieces = pcsper * EDITTEXT_QUANTITY.text
			Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
				total_pieces = dozper * EDITTEXT_QUANTITY.text
			Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
				total_pieces = boxper * EDITTEXT_QUANTITY.text
			Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
				total_pieces = bagper * EDITTEXT_QUANTITY.text
			Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
				total_pieces = packper * EDITTEXT_QUANTITY.text
			End If
			If BUTTON_SAVE.Text = " SAVE" Then
					cursor9 = connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as 'transaction_number' from sales_return_disc_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"'")
					If cursor9.RowCount > 0 Then
						For i = 0 To cursor9.RowCount - 1
							cursor9.Position = i
							If cursor9.GetString("transaction_number") = Null Or cursor9.GetString("transaction_number") = "" Then
								transaction_number = 1
							Else
								transaction_number = cursor9.GetString("transaction_number") + 1
							End If
						Next
					End If
			GET_SALESMAN_ID
			Sleep(100)
					
				If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_WAREHOUSE.cmbBox.IndexOf("MAIN WAREHOUSE") Then
					INPUT_EXPIRY
				Else
					If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("NEAR EXPIRY") Then
						INPUT_EXPIRY
					End If

				End If
					
						Dim query As String = "INSERT INTO sales_return_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
						connection.ExecNonQuery2(query,Array As String(return_id,transaction_number,sales_position_id,CMB_SALESMAN.cmbBox.SelectedItem, _
						principal_id, LABEL_LOAD_PRINCIPAL.Text,CMB_WAREHOUSE.cmbBox.SelectedItem, product_id, LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text, CMB_UNIT.cmbBox.SelectedItem, _
						EDITTEXT_QUANTITY.Text, total_pieces, scan_code, reason, CMB_REASON.cmbBox.SelectedItem ,DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), LOGIN_MODULE.username, LOGIN_MODULE.tab_id))
						Sleep(0)
						ToastMessageShow("Product Return",  False)

						Sleep(0)
						EDITTEXT_QUANTITY.Text = ""
						CMB_WAREHOUSE.SelectedIndex = -1
						OpenSpinner(CMB_WAREHOUSE.cmbBox)
						CLEAR_EXPIRATION
			Else
				Msgbox2Async("Are you sure you want to update this item?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					GET_SALESMAN_ID
					
					Sleep(100)
					If CMB_WAREHOUSE.cmbBox.SelectedIndex = CMB_WAREHOUSE.cmbBox.IndexOf("MAIN WAREHOUSE") Then
							connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"' AND transaction_number = '"&item_number&"'")
							Sleep(0)
							transaction_number = item_number
							INPUT_EXPIRY
					Else
						If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("NEAR EXPIRY") Then
							connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"' AND transaction_number = '"&item_number&"'")
							Sleep(0)
							transaction_number = item_number
							INPUT_EXPIRY
						Else
							connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"' AND transaction_number = '"&item_number&"'")
							ToastMessageShow("Expiry Deleted", True)
						End If
					End If
					Sleep(0)
					Dim insert_query As String = "INSERT INTO sales_return_disc_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_disc_table WHERE return_id = ? AND transaction_number = ?"
					connection.ExecNonQuery2(insert_query, Array As String(DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),LOGIN_MODULE.username,"EDITED",LABEL_LOAD_RETURN_ID.TEXT,item_number))
					Sleep(0)
					Dim query As String = "UPDATE sales_return_disc_table SET salesman_position_id = ?, salesman_name = ?, unit = ?, quantity = ?, total_pieces = ?, warehouse = ?, return_reason = ?, user_info = ?, date_registered = ? , time_registered = ? WHERE return_id = ? and transaction_number = ?"
					connection.ExecNonQuery2(query,Array As String(sales_position_id,CMB_SALESMAN.cmbBox.SelectedItem,CMB_UNIT.cmbBox.SelectedItem, EDITTEXT_QUANTITY.Text, total_pieces,CMB_WAREHOUSE.cmbBox.SelectedItem,CMB_REASON.cmbBox.SelectedItem,LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.now), LABEL_LOAD_RETURN_ID.Text, item_number))
					Sleep(0)
					ToastMessageShow("Transaction Updated", False)
					CLEAR
					Sleep(0)
					LOAD_RETURN_PRODUCT
				End If
			End If

		End If
	Else
		Msgbox2Async("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
End Sub
Sub CLEAR
	item_number = 0
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(0,255,70), 5, 0, Colors.LightGray)
	BUTTON_SAVE.Background = bg
	BUTTON_SAVE.Text = " SAVE"
	EDITTEXT_QUANTITY.Text = ""
	BUTTON_CANCEL.Visible = False
	CMB_REASON.cmbBox.Enabled = True
	PANEL_BG_INPUT.SetVisibleAnimated(300,False)
	CLEAR_EXPIRATION
End Sub

Sub LOAD_RETURN_PRODUCT_HEADER
	NameColumn(0)=TABLE_SALES_RETURN.AddColumn("#", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_SALES_RETURN.AddColumn("Product Variant", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_SALES_RETURN.AddColumn("Product Description", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_SALES_RETURN.AddColumn("Unit", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_SALES_RETURN.AddColumn("Qty", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	NameColumn(5)=TABLE_SALES_RETURN.AddColumn("Warehouse", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	NameColumn(6)=TABLE_SALES_RETURN.AddColumn("Reason", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	NameColumn(7)=TABLE_SALES_RETURN.AddColumn("Expiry", TABLE_SALES_RETURN.COLUMN_TYPE_TEXT)
	
End Sub
Sub LOAD_RETURN_PRODUCT
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor1 = connection.ExecQuery("SELECT a.*, b.date_expired FROM sales_return_disc_table as a LEFT JOIN sales_return_expiry_table as b ON a.return_id = b.return_id AND a.transaction_number = b.transaction_number WHERE a.return_id = '"&return_id&"'")
	If cursor1.RowCount > 0 Then
		For ic = 0 To cursor1.RowCount - 1
			cursor1.Position = ic
			Sleep(10)
			Dim row(8) As Object
			row(0) = cursor1.GetString("transaction_number")
			row(1) = cursor1.GetString("product_variant")
			row(2) = cursor1.GetString("product_description")
			row(3) = cursor1.GetString("unit")
			row(4) = cursor1.GetString("quantity")
			row(5) = cursor1.GetString("warehouse")
			row(6) = cursor1.GetString("return_reason")
			If cursor1.GetString("date_expired") = Null Or cursor1.GetString("date_expired") = "" Then
				row(7) = "-"
			Else
				row(7) = cursor1.GetString("date_expired")
			End If
			Data.Add(row)
		Next
		TABLE_SALES_RETURN.NumberOfFrozenColumns = 1
		TABLE_SALES_RETURN.RowHeight = 50dip
		Sleep(100)
		Sleep(0)
		ProgressDialogHide
	Else
		ProgressDialogHide
	End If
	TABLE_SALES_RETURN.SetData(Data)
	If XSelections.IsInitialized = False Then
	XSelections.Initialize(TABLE_SALES_RETURN)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_SALES_RETURN_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(1),NameColumn(2), NameColumn(3),NameColumn(4), NameColumn(5),NameColumn(6),NameColumn(7))

		Dim MaxWidth As Int
		Dim MaxHeight As Int
		For i = 0 To TABLE_SALES_RETURN.VisibleRowIds.Size
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
	
	For Each Column As B4XTableColumn In Array (NameColumn(3),NameColumn(4))

		For i = 0 To TABLE_SALES_RETURN.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			lbl.Font = xui.CreateDefaultBoldFont(18)
		Next
	Next
	
'	For i = 0 To TABLE_SALES_RETURN.VisibleRowIds.Size - 1
'		Dim RowId As Long = TABLE_SALES_RETURN.VisibleRowIds.Get(i)
'		If RowId > 0 Then
'			Dim pnl1 As B4XView = NameColumn(4).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
'			Dim row As Map = TABLE_SALES_RETURN.GetRow(RowId)
'			Dim clr As Int
'			Dim OtherColumnValue As String = row.Get(NameColumn(4).Id)
'			If OtherColumnValue > 0 Then
'				clr = xui.Color_Red
'			End If
'			pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Colors.RGB(215,215,215), 0)
'			
'		End If
'	Next
	For Each Column As B4XTableColumn In Array (NameColumn(0))
		Column.InternalSortMode= "ASC"
	Next
	If ShouldRefresh Then
		TABLE_SALES_RETURN.Refresh
		XSelections.Clear
	End If
End Sub
Sub TABLE_SALES_RETURN_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Dim RowData As Map = TABLE_SALES_RETURN.GetRow(RowId)
End Sub
Sub TABLE_SALES_RETURN_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = TABLE_SALES_RETURN.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)
'	
	Msgbox2Async("Item : #" & RowData.Get("#") & CRLF & "Product : " & RowData.Get("Product Description")  & CRLF & "Count : " & _
	RowData.Get("Qty")  & " "  & RowData.Get("Unit") & CRLF & "Warehouse : " & RowData.Get("Warehouse") & CRLF  & "Reason : " & RowData.Get("Reason"), _
	"Option", "EDIT", "CANCEL", "DELETE", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
#Region Find Product
		cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&RowData.Get("Product Description")&"'")
		If cursor3.RowCount > 0 Then
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
				
				default_reading = cursor3.GetString("default_expiration_date_reading")
				lifespan_year = cursor3.GetString("life_span_year")
				lifespan_month = cursor3.GetString("life_span_month")
		
			Next
		End If
		Sleep(100)
		cursor4 = connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"' AND transaction_number = '"&RowData.Get("#")&"'")
		If cursor4.RowCount > 0 Then
			For row = 0 To cursor4.RowCount - 1
				cursor4.Position = row
				EDITTEXT_QUANTITY.Text = cursor4.GetString("quantity")
				Sleep(0)
				CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf(cursor4.GetString("unit"))
				Sleep(0)
				CMB_SALESMAN.SelectedIndex = CMB_SALESMAN.cmbBox.IndexOf(cursor4.GetString("salesman_name"))
				Sleep(0)
				CMB_WAREHOUSE.SelectedIndex = CMB_WAREHOUSE.cmbBox.IndexOf(cursor4.GetString("warehouse"))
				Sleep(0)
				LOAD_REASON
				Sleep(0)
				CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.IndexOf(cursor4.GetString("return_reason"))
				LABEL_LOAD_PRINCIPAL.Text = cursor4.Getstring("principal_name")
				item_number = cursor4.GetString("transaction_number")
				scan_code = cursor4.GetString("scan_code")
				reason = cursor4.GetString("input_reason")
				If cursor4.GetString("warehouse") = "MAIN WAREHOUSE" Then
					BUTTON_EXPIRATION.Visible = True
					LOAD_EXPIRATION
				Else
					If cursor4.GetString("return_reason") = "NEAR EXPIRY" Then
						BUTTON_EXPIRATION.Visible = True
						LOAD_EXPIRATION
					Else
						BUTTON_EXPIRATION.Visible = False
					End If

				End If
				
			Next
		Else
		End If

#End Region		

		
		PANEL_BG_INPUT.SetVisibleAnimated(300,True)
		PANEL_BG_INPUT.BringToFront
		
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.RGB(0,167,255), 5, 0, Colors.LightGray)
		BUTTON_SAVE.Background = bg
		BUTTON_SAVE.Text = " Edit"
		BUTTON_CANCEL.Visible = True
		Sleep(0)
		EDITTEXT_QUANTITY.RequestFocus
		Sleep(10)
		EDITTEXT_QUANTITY.SelectAll
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_QUANTITY)

		
	Else If Result = DialogResponse.NEGATIVE Then
		Msgbox2Async("Are you sure you want to delete this item?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			Dim insert_query As String = "INSERT INTO sales_return_disc_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_disc_table WHERE return_id = ? AND transaction_number = ?"
			connection.ExecNonQuery2(insert_query, Array As String(DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),LOGIN_MODULE.username,"DELETED",LABEL_LOAD_RETURN_ID.TEXT,RowData.Get("#")))
			Sleep(0)
			connection.ExecNonQuery("DELETE FROM sales_return_disc_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"' AND transaction_number = '"&RowData.Get("#")&"'")
			Sleep(0)
			connection.ExecNonQuery("DELETE FROM sales_return_expiry_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"' AND transaction_number = '"&RowData.Get("#")&"'")
			ToastMessageShow("Deleted Successfully", True)
			Sleep(0)
			LOAD_RETURN_PRODUCT
		End If
	End If
End Sub

Sub LOG_RETURN_UPLOAD
	connection.ExecNonQuery("DELETE FROM return_upload_table WHERE return_id = '"&return_id&"'")
	Sleep(0)
	Dim query As String = "INSERT INTO return_upload_table VALUES (?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(LABEL_LOAD_RETURN_ID.Text,DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), LOGIN_MODULE.username, LOGIN_MODULE.tab_id))
End Sub
Sub LOAD_RETURN_UPLOAD
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LVL_RID.Background = bg
	LVL_RID.Clear
	Sleep(0)
	LVL_RID.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LVL_RID.TwoLinesLayout.Label.TextSize = 16
	LVL_RID.TwoLinesLayout.SecondLabel.Top = -1%y
	LVL_RID.TwoLinesLayout.label.Height = 6%y
	LVL_RID.TwoLinesLayout.Label.TextColor = Colors.Black
	LVL_RID.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LVL_RID.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LVL_RID.TwoLinesLayout.SecondLabel.Top = 4.5%y
	LVL_RID.TwoLinesLayout.SecondLabel.TextSize = 11
	LVL_RID.TwoLinesLayout.SecondLabel.Height = 3%y
	LVL_RID.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LVL_RID.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LVL_RID.TwoLinesLayout.ItemHeight = 8%y
	Sleep(0)
		cursor3 = connection.ExecQuery("SELECT a.*,b.upload_date,b.upload_time, b.user_info as 'upload_user' FROM sales_return_ref_table as a " & _
		"LEFT JOIN return_upload_table As b " & _
		"ON a.return_id = b.return_id WHERE a.return_route_id = '"&RETURN_MODULE.return_route_id&"'")
		If cursor3.RowCount > 0 Then
		For rows= 0 To cursor3.RowCount - 1
			cursor3.Position = rows
			If cursor3.GetString("upload_date") = Null Or cursor3.GetString("upload_date") = "" Then
				LVL_RID.AddTwoLines2(cursor3.GetString("return_id")  ,"Customer : " & cursor3.GetString("customer_name") & " Status : Saved",cursor3.GetString("return_id"))
			Else
				LVL_RID.AddTwoLines2(cursor3.GetString("return_id") ,"Customer : " & cursor3.GetString("customer_name") & " Status : Uploaded",cursor3.GetString("return_id"))
			End If
		Next
	End If
	PANEL_BG_RID.SetVisibleAnimated(300,True)
	PANEL_BG_RID.BringToFront
End Sub
Sub BUTTON_RID_EXIT_Click
	PANEL_BG_RID.SetVisibleAnimated(300,False)
End Sub

Sub LVL_RID_ItemClick (Position As Int, Value As Object)
	
	Msgbox2Async("Return ID : " & Value ,"Option", "ENTER", "CANCEL", "UPLOAD", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		cursor3 = connection.ExecQuery("SELECT * FROM sales_return_ref_table WHERE return_id = '"&Value&"'")
		If cursor3.RowCount > 0 Then
			For rows= 0 To cursor3.RowCount - 1
				cursor3.Position = rows
				LABEL_LOAD_CUSTOMER_NAME.Text = cursor3.GetString("customer_name")
				LABEL_LOAD_RETURN_DATE.Text = cursor3.GetString("return_date")
				LABEL_LOAD_RETURN_ID.Text = cursor3.GetString("return_id")
				return_id = cursor3.GetString("return_id")
			Next
			BUTTON_MANUAL.Visible = True
			BUTTON_UPLOAD.Visible = True
			LOAD_RETURN_PRODUCT
			PANEL_BG_RID.SetVisibleAnimated(300,False)
		End If
	else If Result = DialogResponse.NEGATIVE Then
		return_id = Value
		cursor1 = connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"&Value&"'")
		If cursor1.RowCount > 0 Then
			Msgbox2Async("Are you sure you want to upload this return?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				DELETE_RETURN_REF
			Else
		
			End If
		Else
			Msgbox2Async("There's nothing to upload in this transaction.", "Empty Upload", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		
		End If
	End If
End Sub
Sub LVL_RID_ItemLongClick (Position As Int, Value As Object)
	Msgbox2Async("Do you want to delete this Return ID " & Value & "?" ,"Deleting return ID", "DELETE", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		cursor3 = connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"&Value&"'")
		If cursor3.RowCount > 0 Then
			For rows= 0 To cursor3.RowCount - 1
				cursor3.Position = rows
				Dim insert_query As String = "INSERT INTO sales_return_disc_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_disc_table WHERE return_id = ? AND transaction_number = ?"
				connection.ExecNonQuery2(insert_query, Array As String(DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),LOGIN_MODULE.username,"DELETED",cursor3.GetString("return_id"),cursor3.GetString("transaction_number")))
				Sleep(0)
				connection.ExecNonQuery("DELETE FROM sales_return_disc_table WHERE return_id = '"&cursor3.GetString("return_id")&"' AND transaction_number = '"&cursor3.GetString("transaction_number")&"'")
			Next
		End If
		Dim insert_query As String = "INSERT INTO sales_return_ref_table_trail SELECT *, ? as 'date_modified', ? as 'time_modified', ? as 'modified_by', ? as 'modified_type' from sales_return_ref_table WHERE return_id = ?"
		connection.ExecNonQuery2(insert_query, Array As String(DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),LOGIN_MODULE.username,"DELETED",Value))
		Sleep(0)
		connection.ExecNonQuery("DELETE FROM sales_return_ref_table WHERE return_id = '"&Value&"'")
		ToastMessageShow("Deleted Successfully", True)
		LOAD_RETURN_PRODUCT
		LABEL_LOAD_CUSTOMER_NAME.Text = "-"
		LABEL_LOAD_RETURN_DATE.Text = "-"
		LABEL_LOAD_RETURN_ID.Text = "-"
		BUTTON_MANUAL.Visible = False
		BUTTON_UPLOAD.Visible = False
		PANEL_BG_RID.SetVisibleAnimated(300,False)
	End If
End Sub

Sub RETURN_MANUAL
	Sleep(0)
	SearchTemplate.CustomListView1.Clear
	Dialog.Title = "Find Product"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor5 = connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' ORDER BY product_desc ASC")
	For Isq = 0 To cursor5.RowCount - 1
		cursor5.Position = Isq
		Sleep(100)
		Items.Add(cursor5.GetString("product_desc"))
	Next
	SearchTemplate.SetItems(Items)
End Sub
Sub BUTTON_MANUAL_Click
	Dim ls As List
	ls.Initialize
	ls.Add("BARCODE NOT REGISTERED")
	ls.Add("NO ACTUAL BARCODE")
	ls.Add("NO SCANNER")
	ls.Add("DAMAGE BARCODE")
	ls.Add("SCANNER CAN'T READ BARCODE")
	ls.Add("NO ACTUAL STOCK")
	InputListAsync(ls, "Choose reason", -1, True) 'show list with paired devices
	Wait For InputList_Result (Result2 As Int)
	If Result2 <> DialogResponse.CANCEL Then
		Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate, "", "", "CANCEL")
		Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
		Wait For (rs) Complete (Result As Int)
		If Result = xui.DialogResponse_Positive Then
#Region Find Product
			cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&SearchTemplate.SelectedItem&"'")
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
				
				default_reading = cursor3.GetString("default_expiration_date_reading")
				lifespan_year = cursor3.GetString("life_span_year")
				lifespan_month = cursor3.GetString("life_span_month")
		
			Next
			cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
			If cursor6.RowCount > 0 Then
				For row = 0 To cursor6.RowCount - 1
					cursor6.Position = row
					LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring("principal_name")
				Next
			End If
			reason = ls.Get(Result2)
			scan_code = "N/A"
			PANEL_BG_INPUT.SetVisibleAnimated(300,True)
			Sleep(0)
			EDITTEXT_QUANTITY.Text = ""
			CMB_WAREHOUSE.SelectedIndex = -1
			Sleep(0)
			OpenSpinner(CMB_WAREHOUSE.cmbBox)
			CMB_REASON.cmbBox.Clear
			Sleep(0)
			CLEAR_EXPIRATION
#End Region		
		End If
	End If
End Sub

Sub DELETE_RETURN_REF
	PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)
	PANEL_BG_MSGBOX.BringToFront
	LABEL_HEADER_TEXT.Text = "Uploading Data"
	LABEL_MSGBOX2.Text = "Fetching Data..."
	LABEL_MSGBOX1.Text = "Loading, Please wait..."
	Dim cmd As DBCommand = CreateCommand("delete_return_ref", Array(return_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_RETURN_REF
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_RETURN_REF
	error_trigger = 0
	cursor6 = connection.ExecQuery("SELECT * FROM sales_return_ref_table WHERE return_id = '"&return_id&"'")
	If cursor6.RowCount > 0 Then
		For i = 0 To cursor6.RowCount - 1
			cursor6.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_return_ref", Array As String(cursor6.GetString("return_route_id"),cursor6.GetString("return_id"), cursor6.GetString("return_date"),cursor6.GetString("return_number"), _
			cursor6.GetString("customer_id"),cursor6.GetString("customer_name"),cursor6.GetString("date_registered"),cursor6.GetString("time_registered"),cursor6.GetString("user_info"),cursor6.GetString("tab_id")))
			LABEL_MSGBOX2.Text = "Uploading : " & cursor6.GetString("return_id")
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				
			Else
				error_trigger = 1
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Log("INSERT_RETURN_REF ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
			End If
			js.Release
		Next
	End If
	Sleep(1000)
	If error_trigger = 0 Then
		DELETE_RETURN_REF_TRAIL		
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Updating Failed", False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_RETURN_REF_TRAIL
	Dim cmd As DBCommand = CreateCommand("delete_return_ref_trail", Null)
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_RETURN_REF_TRAIL
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_RETURN_REF_TRAIL
	error_trigger = 0
	cursor7 = connection.ExecQuery("SELECT * FROM sales_return_ref_table_trail")
	If cursor7.RowCount > 0 Then
		For i = 0 To cursor7.RowCount - 1
			cursor7.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_return_ref_trail", Array As String(cursor7.GetString("return_route_id"),cursor7.GetString("return_id"), cursor7.GetString("return_date"),cursor7.GetString("return_number"), _
			cursor7.GetString("customer_id"),cursor7.GetString("customer_name"),cursor7.GetString("date_registered"),cursor7.GetString("time_registered"),cursor7.GetString("user_info"),cursor7.GetString("tab_id") _
			,cursor7.GetString("date_modified"),cursor7.GetString("time_modified"),cursor7.GetString("modified_by"),cursor7.GetString("modified_type")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				
			Else
				error_trigger = 1
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Log("INSERT_RETURN_REF_TRAIL ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
			End If
			js.Release
		Next
	End If
	Sleep(1000)
	If error_trigger = 0 Then
		DELETE_RETURN_DISC
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Updating Failed", False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_RETURN_DISC
	Dim cmd As DBCommand = CreateCommand("delete_return_disc", Array(return_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_RETURN_DISC
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_RETURN_DISC
	error_trigger = 0
	cursor8 = connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"&return_id&"'")
	If cursor8.RowCount > 0 Then
		For i = 0 To cursor8.RowCount - 1
			cursor8.Position = i
			Sleep(100)
			Log (cursor8.GetString("product_description"))
			Dim cmd As DBCommand = CreateCommand("insert_return_disc", Array As String(cursor8.GetString("return_id"),cursor8.GetString("transaction_number"), cursor8.GetString("salesman_position_id"),cursor8.GetString("salesman_name"), _
			cursor8.GetString("principal_id"),cursor8.GetString("principal_name"),cursor8.GetString("warehouse"),cursor8.GetString("product_id"),cursor8.GetString("product_variant"), _
			cursor8.GetString("product_description"),cursor8.GetString("unit"),cursor8.GetString("quantity"),cursor8.GetString("total_pieces"),cursor8.GetString("scan_code"),cursor8.GetString("input_reason"), _
			cursor8.GetString("return_reason"),cursor8.GetString("date_registered"),cursor8.GetString("time_registered"),cursor8.GetString("user_info"),cursor8.GetString("tab_id")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor8.GetString("product_description")
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				
			Else
				error_trigger = 1
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Log("INSERT_RETURN_DISC ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
			End If
			js.Release
		Next
	End If
	Sleep(1000)
	If error_trigger = 0 Then
		DELETE_RETURN_DISC_TRAIL
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Updating Failed", False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_RETURN_DISC_TRAIL
	Dim cmd As DBCommand = CreateCommand("delete_return_disc_trail", Array(return_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_RETURN_DISC_TRAIL
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_RETURN_DISC_TRAIL
	error_trigger = 0
	cursor9 = connection.ExecQuery("SELECT * FROM sales_return_disc_table_trail WHERE return_id = '"&return_id&"'")
	If cursor9.RowCount > 0 Then
		For i = 0 To cursor9.RowCount - 1
			cursor9.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_return_disc_trail", Array As String(cursor9.GetString("return_id"),cursor9.GetString("transaction_number"), cursor9.GetString("salesman_position_id"),cursor9.GetString("salesman_name"), _
			cursor9.GetString("principal_id"),cursor9.GetString("principal_name"),cursor9.GetString("warehouse"),cursor9.GetString("product_id"),cursor9.GetString("product_variant"), _
			cursor9.GetString("product_description"),cursor9.GetString("unit"),cursor9.GetString("quantity"),cursor9.GetString("total_pieces"),cursor9.GetString("scan_code"),cursor9.GetString("input_reason"), _
			cursor9.GetString("return_reason"),cursor9.GetString("date_registered"),cursor9.GetString("time_registered"),cursor9.GetString("user_info"),cursor9.GetString("tab_id"), _
			cursor9.GetString("date_modified"),cursor9.GetString("time_modified"),cursor9.GetString("modified_by"),cursor9.GetString("modified_type")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor9.GetString("product_description")
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				
			Else
				error_trigger = 1
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Log("INSERT_RETURN_DISC_TRAIL ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
			End If
			js.Release
		Next
	End If
	Sleep(1000)
	If error_trigger = 0 Then
		DELETE_RETURN_EXPIRY

	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Updating Failed", False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_RETURN_EXPIRY
	Dim cmd As DBCommand = CreateCommand("delete_return_expiry", Array(return_id))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_RETURN_EXPIRY
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Uploading Failed", False)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Uploading Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_RETURN_EXPIRY
	error_trigger = 0
	cursor2 = connection.ExecQuery("SELECT * FROM sales_return_expiry_table WHERE return_id = '"&return_id&"'")
	If cursor2.RowCount > 0 Then
		For i = 0 To cursor2.RowCount - 1
			cursor2.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_return_expiry", Array As String(cursor2.GetString("return_id"),cursor2.GetString("transaction_number"), _
			cursor2.GetString("principal_id"),cursor2.GetString("principal_name"),cursor2.GetString("warehouse"),cursor2.GetString("product_id"),cursor2.GetString("product_variant"), _
			cursor2.GetString("product_description"),cursor2.GetString("unit"),cursor2.GetString("quantity"),cursor2.GetString("total_pieces"), _
			cursor2.GetString("month_expired"),cursor2.GetString("year_expired"),cursor2.GetString("date_expired"),cursor2.GetString("month_manufactured"),cursor2.GetString("year_manufactured"),cursor2.GetString("date_manufactured"), _
			cursor2.GetString("scan_code"),cursor2.GetString("input_reason"),cursor2.GetString("date_registered"),cursor2.GetString("time_registered"),cursor2.GetString("user_info"),cursor2.GetString("tab_id"),"EXISTING"))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor2.GetString("product_description")
			Wait For(js) JobDone(js As HttpJob)
			If js.Success Then
				
			Else
				error_trigger = 1
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Uploading Failed", False)
				Log("ERROR: " & js.ErrorMessage)
				Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				ToastMessageShow("Uploading Error", False)
				Sleep(1000)
			End If
			js.Release
		Next
		If error_trigger = 0 Then
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			LABEL_MSGBOX2.Text = "Uploaded Successfully..."
			ToastMessageShow("Uploaded Successfully", False)
			LOG_RETURN_UPLOAD
			Sleep(0)
			LABEL_LOAD_CUSTOMER_NAME.Text = "-"
			LABEL_LOAD_RETURN_DATE.Text = "-"
			LABEL_LOAD_RETURN_ID.Text = "-"
			return_id = "-"
			LOAD_RETURN_PRODUCT
			BUTTON_MANUAL.Visible = False
			BUTTON_UPLOAD.Visible = False
			PANEL_BG_RID.SetVisibleAnimated(300,False)
			Sleep(0)
			LOAD_RETURN_UPLOAD
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				DELETE_RETURN_REF
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		LABEL_MSGBOX2.Text = "Uploaded Successfully..."
		ToastMessageShow("Uploaded Successfully", False)
		LOG_RETURN_UPLOAD
		Sleep(0)
		LABEL_LOAD_CUSTOMER_NAME.Text = "-"
		LABEL_LOAD_RETURN_DATE.Text = "-"
		LABEL_LOAD_RETURN_ID.Text = "-"
		return_id = "-"
		LOAD_RETURN_PRODUCT
		BUTTON_MANUAL.Visible = False
		BUTTON_UPLOAD.Visible = False
		PANEL_BG_RID.SetVisibleAnimated(300,False)
		Sleep(0)
		LOAD_RETURN_UPLOAD
	End If
End Sub
Sub BUTTON_UPLOAD_Click
	return_id = LABEL_LOAD_RETURN_ID.Text
	cursor1 = connection.ExecQuery("SELECT * FROM sales_return_disc_table WHERE return_id = '"&return_id&"'")
	If cursor1.RowCount > 0 Then
		Msgbox2Async("Are you sure you want to upload this return?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RETURN_REF
		Else
		
		End If
	Else
		Msgbox2Async("There's nothing to upload in this transaction.", "Empty Upload", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		
	End If
End Sub

Sub LABEL_LOAD_EXPIRATION_Click
	Dialog.Title = "Select Expiration Date"
	Dialog.TitleBarColor = Colors.RGB(255,109,81)
	Wait For (Dialog.ShowTemplate(DateTemplate, "", "NO EXP", "CANCEL")) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_EXPIRATION.Text = DateTime.Date(DateTemplate.Date)
'		OpenSpinner(CMB_UNIT.cmbBox)
'		Sleep(10)
'		CMB_UNIT.SelectedIndex = -1
		GET_EXPIRATION_SPAN
	Else if Result = xui.DialogResponse_Negative Then
		LABEL_LOAD_EXPIRATION.Text = "-"
	End If
End Sub
Sub LABEL_LOAD_MANUFACTURED_Click
	Dialog.Title = "Select Manufacturing Date"
	Dialog.TitleBarColor = Colors.RGB(91,255,81)
	Wait For (Dialog.ShowTemplate(DateTemplate2, "", "NO MFG", "CANCEL")) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(DateTemplate2.Date)
'		OpenSpinner(CMB_UNIT.cmbBox)
'		Sleep(10)
'		CMB_UNIT.SelectedIndex = -1
		GET_MANUFACTURING_SPAN
	Else if Result = xui.DialogResponse_Negative Then
		LABEL_LOAD_MANUFACTURED.Text = "-"
	End If
End Sub
Sub GET_EXP
	If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "01" Then
		monthexp = "January"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "02" Then
		monthexp = "February"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "03" Then
		monthexp = "March"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "04" Then
		monthexp = "April"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "05" Then
		monthexp = "May"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "06" Then
		monthexp = "June"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "07" Then
		monthexp = "July"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "08" Then
		monthexp = "August"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "09" Then
		monthexp = "September"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "10" Then
		monthexp = "October"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "11" Then
		monthexp = "November"
	Else If LABEL_LOAD_EXPIRATION.Text.SubString2(5,7) = "12" Then
		monthexp = "December"
	Else
		monthexp = "NO EXPIRATION"
	End If
	
	yearexp = LABEL_LOAD_EXPIRATION.Text.SubString2(0,4)
End Sub
Sub GET_MFG
	If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "01" Then
		monthmfg = "January"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "02" Then
		monthmfg = "February"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "03" Then
		monthmfg = "March"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "04" Then
		monthmfg = "April"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "05" Then
		monthmfg = "May"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "06" Then
		monthmfg = "June"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "07" Then
		monthmfg = "July"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "08" Then
		monthmfg = "August"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "09" Then
		monthmfg = "September"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "10" Then
		monthmfg = "October"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "11" Then
		monthmfg = "November"
	Else If LABEL_LOAD_MANUFACTURED.Text.SubString2(5,7) = "12" Then
		monthmfg = "December"
	Else
		monthmfg = "NO MANUFACTURING"
	End If
	
	yearmfg = LABEL_LOAD_MANUFACTURED.Text.SubString2(0,4)
End Sub
Sub GET_EXPIRATION_SPAN
	Dim days_year As String
	Dim days_month As String
	
	If lifespan_year <> "" Then
		days_year = lifespan_year.SubString2(0,lifespan_year.IndexOf("Y")-1)
	Else
		days_year = 0
	End If
	
	If lifespan_month <> "" Then
		days_month = lifespan_month.SubString2(0,lifespan_month.IndexOf("M")-1)
	Else
		days_month = 0
	End If
	
	Log(days_year & " " &days_month)

	If LABEL_LOAD_EXPIRATION.Text <> "NO EXPIRATION" Then
		If lifespan_year = "" And lifespan_month = "" Then
'			LABEL_LOAD_MANUFACTURED.Text = "NO MANUFACTURING"
		Else
			Dim manufacturing As Long = DateTime.Add(DateTime.DateParse(LABEL_LOAD_EXPIRATION.Text),-days_year,-days_month,0)
			LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(manufacturing)
		End If
	End If
End Sub
Sub GET_MANUFACTURING_SPAN
	Dim days_year As String
	Dim days_month As String
	
	If lifespan_year <> "" Then
		days_year = lifespan_year.SubString2(0,lifespan_year.IndexOf("Y")-1)
	Else
		days_year = 0
	End If
	
	If lifespan_month <> "" Then
		days_month = lifespan_month.SubString2(0,lifespan_month.IndexOf("M")-1)
	Else
		days_month = 0
	End If
	
	Log(days_year & " " &days_month)

	If LABEL_LOAD_MANUFACTURED.Text <> "NO EXPIRATION" Then
		If lifespan_year = "" And lifespan_month = "" Then
'			LABEL_LOAD_MANUFACTURED.Text = "NO MANUFACTURING"
		Else
			Dim expiration As Long = DateTime.Add(DateTime.DateParse(LABEL_LOAD_MANUFACTURED.Text),days_year,days_month,0)
			LABEL_LOAD_EXPIRATION.Text = DateTime.Date(expiration)
		End If
	End If
End Sub
Sub BUTTON_EXPIRATION_EXIT_Click
	If BUTTON_SAVE.Text = " SAVE" Then
	CLEAR_EXPIRATION
	PANEL_BG_EXPIRATION.SetVisibleAnimated(300, False)
	Sleep(0)
	CMB_REASON.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_REASON.cmbBox)
	ToastMessageShow("Near expiry cancel", False)
	Else
		LOAD_EXPIRATION
		PANEL_BG_EXPIRATION.SetVisibleAnimated(300, False)
	End If
End Sub
Sub BUTTON_EXPIRATION_CONFIRM_Click
	If LABEL_LOAD_EXPIRATION.Text = "-" Or LABEL_LOAD_MANUFACTURED.Text = "-" Then
		ToastMessageShow("Please input a product first.", False)
	Else
		If BUTTON_SAVE.Text = " SAVE" Then
		INPUT_NEAR_EXPIRY
		Else
		PANEL_BG_EXPIRATION.SetVisibleAnimated(300, False)
		BUTTON_EXPIRATION.Visible = True
		End If
	End If
End Sub
Sub CLEAR_EXPIRATION
	LABEL_LOAD_EXPIRATION.Text = "-"
	LABEL_LOAD_MANUFACTURED.Text = "-"
	BUTTON_EXPIRATION.Visible = False
End Sub
Sub BUTTON_EXPIRATION_Click
	PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)
	PANEL_BG_EXPIRATION.BringToFront
End Sub
Sub DaysBetweenDates(Date1 As Long, Date2 As Long)
	Return Floor((Date2 - Date1) / DateTime.TicksPerDay)
End Sub
Sub INPUT_NEAR_EXPIRY
	If LABEL_LOAD_EXPIRATION.Text <> "-" Then
		Dim EXPDATE As Long = DateTime.DateParse(LABEL_LOAD_EXPIRATION.Text)
		Dim  DATENOW As Long = DateTime.DateParse(DateTime.Date(DateTime.Now))
		If DaysBetweenDates(DATENOW,EXPDATE) <= 0 Then
			ToastMessageShow("You cannot input a expiration date from to date or back date.", False)
		Else
			PANEL_BG_EXPIRATION.SetVisibleAnimated(300, False)
			Sleep(0)
			CMB_UNIT.SelectedIndex = -1
			OpenSpinner(CMB_UNIT.cmbBox)
			BUTTON_EXPIRATION.Visible = True
		End If
	Else
			
	End If
		
End Sub
Sub INPUT_EXPIRY
	GET_EXP
	GET_MFG
	Sleep(0)
	Dim query As String = "INSERT INTO sales_return_expiry_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
		connection.ExecNonQuery2(query,Array As String(LABEL_LOAD_RETURN_ID.Text,transaction_number,principal_id,LABEL_LOAD_PRINCIPAL.Text, _
		CMB_WAREHOUSE.cmbBox.SelectedItem,product_id,LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text,CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QUANTITY.Text,total_pieces, _
		monthexp,yearexp,LABEL_LOAD_EXPIRATION.Text,monthmfg,yearmfg,LABEL_LOAD_MANUFACTURED.Text, scan_code, reason,  _
		DateTime.Date(DateTime.now),DateTime.Time(DateTime.Now),LOGIN_MODULE.username,LOGIN_MODULE.tab_id))
	Sleep(0)
	ToastMessageShow("Product Expiration Added Succesfully",False)
End Sub
Sub LOAD_EXPIRATION
	cursor2 = connection.ExecQuery("SELECT * FROM sales_return_expiry_table WHERE return_id = '"&LABEL_LOAD_RETURN_ID.Text&"' AND transaction_number = '"&item_number&"'")
	If cursor2.RowCount > 0 Then
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			
			LABEL_LOAD_EXPIRATION.Text = cursor2.GetString("date_expired")
			LABEL_LOAD_MANUFACTURED.Text = cursor2.GetString("date_manufactured")
		Next
		BUTTON_EXPIRATION.Visible = True
	Else
	End If
End Sub