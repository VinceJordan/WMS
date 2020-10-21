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
	
	'bluetooth
	Dim serial1 As Serial
	Dim AStream As AsyncStreams
	Dim Ts As Timer
	
	Dim cartBitmap As Bitmap
	Dim addBitmap As Bitmap
	Dim uploadBitmap As Bitmap
	
	'string
	Dim picklist_id As String
	Dim date_dispatch As String
	Dim route_name As String
	Dim plate_no As String
	Dim customer_id As String
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
	Dim cancelled_reason As String
	
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
	Private NameColumn(5) As B4XTableColumn
	
	
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
	
	Dim Invoice As List

	Private LABEL_LOAD_CUSTOMER_NAME As Label
	Private LABEL_LOAD_INVOICE_NO As Label
	Private LABEL_LOAD_INVOICE_DATE As Label
	Private TABLE_INVOICE_PRODUCT As B4XTable
	Private PANEL_BG_INPUT As Panel
	Private EDITTEXT_QUANTITY As EditText
	Private CMB_UNIT As B4XComboBox
	Private CMB_REASON As B4XComboBox
	Private LABEL_LOAD_DESCRIPTION As Label
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_PRINCIPAL As Label
	Private PANEL_LIST As Panel
	Private BUTTON_LIST As Button
	Private LVL_LIST As ListView
	Private BUTTON_SAVE As Button
	Private BUTTON_CANCEL As Button
	Private LABEL_LOAD_WRONGSERVED_DESCRIPTION As Label
	Private LABEL_LOAD_WRONGSERVED_VARIANT As Label
	Private LABEL_LOAD_WRONGSERVED_PRINCIPAL As Label
	Private PANEL_BG_WRONGSERVED As Panel
	Private BUTTON_WRONGSERVED As Button
	Private LABEL_LOAD_MANUFACTURED As Label
	Private LABEL_LOAD_EXPIRATION As Label
	Private PANEL_BG_EXPIRATION As Panel
	Private BUTTON_EXPIRATION As Button
	Private BUTTON_UPLOAD As Button
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_MSGBOX2 As Label
	Private LABEL_MSGBOX1 As Label
	Private LABEL_HEADER_TEXT As Label
	Private LVL_INVOICE As ListView
	Private PANEL_BG_INVOICE As Panel
	Private BUTTON_CANCELLED_ALL As Button
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
	Activity.LoadLayout("cancelled")

	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	addBitmap = LoadBitmap(File.DirAssets, "invoice.png")
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
	
'	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Transparent)
'	EDITTEXT_QUANTITY.Background = bg
	WRONGSERVED_MANUAL
	Sleep(100)
	LOAD_INVOICE_PRODUCT_HEADER
	GET_INVOICE
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "See Upload", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("See Upload", uploadBitmap)
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "cart", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(2, 2, "Choose Invoice", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Choose Invoice", addBitmap)
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
	Msgbox2Async("Are you sure you want to exit?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
	Activity.Finish
	StartActivity(RETURN_MODULE)
	SetAnimation("left_to_center", "center_to_right")
	End If
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
	Else If Item.Title = "Choose Invoice" Then
		OPEN_INVOICE
	Else if Item.Title = "See Upload" Then
		LOAD_INVOICE_UPLOAD
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
	Log("Received: " & BytesToString(Buffer, 0, Buffer.Length, "UTF8"))
'	LABEL_LOAD_BARCODE.Text = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	Dim trigger As Int = 0
	
	
	If PANEL_BG_WRONGSERVED.Visible = True Then
#Region Wrong Served
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
				LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = ls.Get(Result)
				trigger = 0
			Else
				trigger = 1
			End If
					
			'SINGLE SKU
		Else if cursor2.RowCount = 1 Then
			For row = 0 To cursor2.RowCount - 1
				cursor2.Position = row
				Log(1)
				LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = cursor2.GetString("product_desc")
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
			cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text&"'")
			For qrow = 0 To cursor3.RowCount - 1
				cursor3.Position = qrow
				LABEL_LOAD_WRONGSERVED_VARIANT.Text = cursor3.GetString("product_variant")
				wrong_principal_id = cursor3.GetString("principal_id")
				wrong_product_id = cursor3.GetString("product_id")
			
				wrong_caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
				wrong_pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
				wrong_dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
				wrong_boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
				wrong_bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
				wrong_packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
			Next
			cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & wrong_principal_id & "'")
			If cursor6.RowCount > 0 Then
				For row = 0 To cursor6.RowCount - 1
					cursor6.Position = row
					LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = cursor6.Getstring("principal_name")
				Next
			End If
			Sleep(0)
			wrong_reason = "N/A"
			wrong_scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
		End If
#end region
	Else
#Region Scan Barcode
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
	cursor4 = connection.ExecQuery("SELECT * FROM cancelled_invoice_ref_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' And product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
	If cursor4.RowCount > 0 Then
		For row = 0 To cursor4.RowCount - 1
			cursor4.Position = row
			total_invoice = cursor4.GetString("total_pieces")
		Next
	Else
		PANEL_BG_INPUT.SetVisibleAnimated(300,False)
		Msgbox2Async("The product you scanned :"& CRLF &""&LABEL_LOAD_DESCRIPTION.Text&" "& CRLF &"is not in the invoice.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		trigger = 1
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
		Sleep(0)
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
		EDITTEXT_QUANTITY.Background = bg
		EDITTEXT_QUANTITY.Text = ""
		Sleep(0)
		LOAD_CANCELLED_REASON
		Sleep(100)
		CMB_REASON.SelectedIndex = -1
		OpenSpinner(CMB_REASON.cmbBox)
		reason = "N/A"
		scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
		PANEL_BG_INPUT.SetVisibleAnimated(300,True)
		cancelled_trigger = 0
		Sleep(0)
		GET_CANCELLED_COUNT
		CLEAR_WRONGSERVED
		CLEAR_EXPIRATION
	End If
#End Region
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
Sub GET_INVOICE
	ProgressDialogShow2("Getting Invoice...", False)
	SearchTemplate.CustomListView1.Clear
	Dialog.Title = "Select Invoice"
	Dialog.TitleBarColor = Colors.RGB(82,168,255)
	Invoice.Initialize
	Invoice.Clear
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_cancelled_pick", Array As String(RETURN_MODULE.route_name,RETURN_MODULE.date_return,RETURN_MODULE.plate_no))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				
				
				Dim req1 As DBRequestManager = CreateRequest
				Dim cmd1 As DBCommand = CreateCommand("select_cancelled_invoice", Array As String(row(res.Columns.Get("picklist_id"))))
				Wait For (req1.ExecuteQuery(cmd1, 0, Null)) JobDone(js As HttpJob)
				If js.Success Then
					req1.HandleJobAsync(js, "req1")
					Wait For (req1) req1_Result(res1 As DBResult)
					If res1.Rows.Size > 0 Then
						For Each row1() As Object In res1.Rows
							Invoice.Add(row1(res1.Columns.Get("InvoiceNo")))
						Next
						BUTTON_UPLOAD.Visible = False
						BUTTON_CANCELLED_ALL.Visible = False
					Else
						Msgbox2Async("This picklist have no exisiting invoice", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					End If
				Else
					ProgressDialogHide
					Log("ERROR: " & js.ErrorMessage)
					Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					ToastMessageShow("Updating Error", False)
				End If
				js.Release
			Next
			SearchTemplate.SetItems(Invoice)
			ProgressDialogHide
			OPEN_INVOICE
		Else
			ProgressDialogHide
			Msgbox2Async("Picklist Route is empty, Please advice IT for this conflict.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
	Else
		ProgressDialogHide
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	ProgressDialogHide
	jr.Release
	
	For rows = 0 To Invoice.Size - 1
	Next
End Sub
#End Region

Sub OPEN_INVOICE
	ProgressDialogShow2("Loading...", False)
	Sleep(1000)
	Dialog.Title = "Select Invoice"
	Dialog.TitleBarColor = Colors.RGB(82,168,255)
	Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate, "", "", "CANCEL")
	Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
	ProgressDialogHide
	Wait For (rs) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		GET_INVOICE_PRODUCTS
	Else
		
	End If
End Sub
Sub GET_INVOICE_PRODUCTS
	
	Dim invoice_no As String = SearchTemplate.SelectedItem.ToUpperCase
	ProgressDialogShow2("Getting Invoice Products...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_invoice_product", Array As String(invoice_no))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		connection.ExecNonQuery("DELETE FROM cancelled_invoice_ref_table WHERE invoice_no = '"&invoice_no&"'")
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				customer_id = row(res.Columns.Get("customer"))
				LABEL_LOAD_CUSTOMER_NAME.Text = row(res.Columns.Get("account_customer_name"))
				LABEL_LOAD_INVOICE_NO.Text = row(res.Columns.Get("invoice_no"))
				LABEL_LOAD_INVOICE_DATE.Text = DateTime.Date( row(res.Columns.Get("invoice_date")))
				connection.ExecNonQuery("INSERT INTO cancelled_invoice_ref_table VALUES ('"&row(res.Columns.Get("customer"))&"','"&row(res.Columns.Get("account_customer_name"))&"','"&row(res.Columns.Get("invoice_no"))& _
				"','"&DateTime.Date( row(res.Columns.Get("invoice_date")))&"','"&row(res.Columns.Get("prod_id"))&"','"&row(res.Columns.Get("prod_brand"))&"','"&row(res.Columns.Get("prod_desc"))& _
				"','"&row(res.Columns.Get("unit"))&"','"&row(res.Columns.Get("qty"))&"','"&row(res.Columns.Get("total_pcs"))&"','"&DateTime.Date(DateTime.Now) &" " &DateTime.Time(DateTime.Now)&"','"&LOGIN_MODULE.username&"','"&LOGIN_MODULE.tab_id&"')")
			Next
			LOAD_INVOICE_PRODUCT
		Else
			ProgressDialogHide
			Msgbox2Async("Invoice is empty, Please advice IT for this conflict.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
	Else
		ProgressDialogHide
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	ProgressDialogHide
	jr.Release
End Sub

Sub LOAD_INVOICE_PRODUCT_HEADER
	NameColumn(0)=TABLE_INVOICE_PRODUCT.AddColumn("Product Variant", TABLE_INVOICE_PRODUCT.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_INVOICE_PRODUCT.AddColumn("Product Description", TABLE_INVOICE_PRODUCT.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_INVOICE_PRODUCT.AddColumn("Unit", TABLE_INVOICE_PRODUCT.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_INVOICE_PRODUCT.AddColumn("Invoiced", TABLE_INVOICE_PRODUCT.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_INVOICE_PRODUCT.AddColumn("Cancelled", TABLE_INVOICE_PRODUCT.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_INVOICE_PRODUCT
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor1 = connection.ExecQuery("SELECT a.*, b.quantity as 'invoice_qty' FROM (SELECT *,sum(quantity) as 'cancelled_qty' FROM cancelled_invoice_disc_table GROUP BY invoice_no,product_id,unit) as a " & _
    	"LEFT JOIN cancelled_invoice_ref_table As b " & _
		"ON a.invoice_no = b.invoice_no AND a.product_id = b.product_id AND a.unit = b.unit " & _
		"WHERE a.invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' ORDER BY a.product_variant, a.product_description ASC")
	If cursor1.RowCount > 0 Then
		For ic = 0 To cursor1.RowCount - 1
			cursor1.Position = ic
			Sleep(10)
			If cursor1.GetString("invoice_qty") = Null Or cursor1.GetString("invoice_qty") = "" Then
				Dim row(5) As Object
				row(0) = cursor1.GetString("product_variant")
				row(1) = cursor1.GetString("product_description")
				row(2) = cursor1.GetString("unit")
				row(3) = 0
				row(4) = cursor1.GetString("cancelled_qty")
				Data.Add(row)
			End If
		Next
	Else
						
	End If
	cursor5 = connection.ExecQuery("SELECT a.*, b.total_quantity as 'cancelled_qty' FROM cancelled_invoice_ref_table as a " & _
    "LEFT JOIN (SELECT *,sum(quantity) as 'total_quantity' FROM cancelled_invoice_disc_table GROUP BY invoice_no,product_id,unit) As b " & _
	"ON a.invoice_no = b.invoice_no AND a.product_id = b.product_id AND a.unit = b.unit " & _
	"WHERE a.invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' ORDER BY a.product_variant, a.product_description ASC")
	If cursor5.RowCount > 0 Then
		For ia = 0 To cursor5.RowCount - 1
			Sleep(10)
			cursor5.Position = ia
			If cursor5.GetString("cancelled_qty") = Null Or cursor5.GetString("cancelled_qty") = "" Then
				Sleep(10)
				Dim row(5) As Object
				row(0) = cursor5.GetString("product_variant")
				row(1) = cursor5.GetString("product_description")
				row(2) = cursor5.GetString("unit")
				row(3) = cursor5.GetString("quantity")
				row(4) = 0
				Data.Add(row)
			Else
				Dim row(5) As Object
				row(0) = cursor5.GetString("product_variant")
				row(1) = cursor5.GetString("product_description")
				row(2) = cursor5.GetString("unit")
				row(3) = cursor5.GetString("quantity")
				row(4) = cursor5.GetString("cancelled_qty")
				Data.Add(row)
			End If
		Next
'		TABLE_INVOICE_PRODUCT.NumberOfFrozenColumns = 1
		TABLE_INVOICE_PRODUCT.RowHeight = 50dip
		Sleep(100)
'		GET_STATUS
		Sleep(0)
		ProgressDialogHide
		BUTTON_UPLOAD.Visible = True
		BUTTON_CANCELLED_ALL.Visible = True
	Else
		ToastMessageShow("Invoice is empty", False)
	End If
	TABLE_INVOICE_PRODUCT.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_INVOICE_PRODUCT)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_INVOICE_PRODUCT_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(1),NameColumn(2), NameColumn(3),NameColumn(4))

		Dim MaxWidth As Int
		Dim MaxHeight As Int
		For i = 0 To TABLE_INVOICE_PRODUCT.VisibleRowIds.Size
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
	
	For Each Column As B4XTableColumn In Array (NameColumn(2), NameColumn(3),NameColumn(4))

		For i = 0 To TABLE_INVOICE_PRODUCT.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			lbl.Font = xui.CreateDefaultBoldFont(18)
		Next
	Next
	
	For i = 0 To TABLE_INVOICE_PRODUCT.VisibleRowIds.Size - 1
		Dim RowId As Long = TABLE_INVOICE_PRODUCT.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(4).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = TABLE_INVOICE_PRODUCT.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(4).Id)
			If OtherColumnValue > 0 Then
				clr = xui.Color_Red
			End If
			pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Colors.RGB(215,215,215), 0)
			
		End If
	Next
	For Each Column As B4XTableColumn In Array (NameColumn(0))
		Column.InternalSortMode= "ASC"
	Next
	If ShouldRefresh Then
		TABLE_INVOICE_PRODUCT.Refresh
		XSelections.Clear
	End If
End Sub
Sub TABLE_INVOICE_PRODUCT_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Dim RowData As Map = TABLE_INVOICE_PRODUCT.GetRow(RowId)
End Sub
Sub TABLE_INVOICE_PRODUCT_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = TABLE_INVOICE_PRODUCT.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)
	
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
		cursor4 = connection.ExecQuery("SELECT * FROM cancelled_invoice_ref_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' And product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
		If cursor4.RowCount > 0 Then
			For row = 0 To cursor4.RowCount - 1
				cursor4.Position = row
				total_invoice = cursor4.GetString("total_pieces")
			Next
		Else
			PANEL_BG_INPUT.SetVisibleAnimated(300,False)
			Msgbox2Async("The product you scanned :"& CRLF &""&LABEL_LOAD_DESCRIPTION.Text&" "& CRLF &"is not in the invoice.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
		cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
		If cursor6.RowCount > 0 Then
			For row = 0 To cursor6.RowCount - 1
				cursor6.Position = row
				LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring("principal_name")
			Next
		End If
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
		EDITTEXT_QUANTITY.Background = bg
		reason = ls.Get(Result2)
		CLEAR_WRONGSERVED
		CLEAR_EXPIRATION
		scan_code = "N/A"
		PANEL_BG_INPUT.SetVisibleAnimated(300,True)
		Sleep(0)
		EDITTEXT_QUANTITY.Text = ""
		CMB_REASON.SelectedIndex = -1
		OpenSpinner(CMB_REASON.cmbBox)
		cancelled_trigger = 0
		LOAD_CANCELLED_REASON
		Sleep(0)
		GET_CANCELLED_COUNT
#End Region		
	End If

End Sub

Sub BUTTON_EXIT_Click
	PANEL_BG_INPUT.SetVisibleAnimated(300,False)
	CLEAR
	Sleep(0)
	LOAD_INVOICE_PRODUCT
End Sub
Sub LOAD_CANCELLED_REASON
	CMB_REASON.cmbBox.Clear
	CMB_REASON.cmbBox.Add("NOT ORDERED")
	CMB_REASON.cmbBox.Add("OVERSTOCK")
	CMB_REASON.cmbBox.Add("NEAR EXPIRY")
	CMB_REASON.cmbBox.Add("EXPIRED")
	CMB_REASON.cmbBox.Add("DAMAGED")
	CMB_REASON.cmbBox.Add("SHORT RETURN")
	CMB_REASON.cmbBox.Add("WRONG SERVED")
	CMB_REASON.cmbBox.Add("REFUSAL")
	CMB_REASON.cmbBox.Add("CLOSED STORE")
	CMB_REASON.cmbBox.Add("NO CASH OR CHEQUE AVAILABLE")
End Sub

Sub BUTTON_LIST_Click
	If cancelled_trigger = 0 Then
		PANEL_LIST.SetVisibleAnimated(300,True)
		PANEL_LIST.BringToFront
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.RGB(0,127,255),5,0,Colors.Black)
		BUTTON_LIST.Background = bg
		BUTTON_LIST.TextColor = Colors.White
		cancelled_trigger = 1
		LOAD_LIST
	Else if cancelled_trigger = 1 Then
		PANEL_LIST.SetVisibleAnimated(300,False)
		PANEL_LIST.BringToFront
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.White,5,0,Colors.Black)
		BUTTON_LIST.Background = bg
		BUTTON_LIST.TextColor = Colors.RGB(82,169,255)
		cancelled_trigger = 0
	End If
End Sub
Sub LOAD_LIST
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LVL_LIST.Background = bg
	LVL_LIST.Clear
	Sleep(0)
	LVL_LIST.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LVL_LIST.TwoLinesLayout.Label.TextSize = 20
	LVL_LIST.TwoLinesLayout.SecondLabel.Top = -1%y
	LVL_LIST.TwoLinesLayout.label.Height = 6%y
	LVL_LIST.TwoLinesLayout.Label.TextColor = Colors.Black
	LVL_LIST.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LVL_LIST.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LVL_LIST.TwoLinesLayout.SecondLabel.Top = 4.3%y
	LVL_LIST.TwoLinesLayout.SecondLabel.TextSize = 14
	LVL_LIST.TwoLinesLayout.SecondLabel.Height = 3%y
	LVL_LIST.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LVL_LIST.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LVL_LIST.TwoLinesLayout.ItemHeight = 8%y
	
	cursor2 = connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.text&"' ORDER BY transaction_number ASC")
	If cursor2.RowCount > 0 Then
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			Sleep(0)
			If cursor2.GetString("cancelled_reason") = "WRONG SERVED" Then
				cursor3 = connection.ExecQuery("SELECT * FROM wrong_served_table WHERE cancelled_id = '"& cursor2.GetString("cancelled_id") &"'")
				If cursor3.RowCount > 0 Then
					For rows= 0 To cursor3.RowCount - 1
						cursor3.Position = rows
						LVL_LIST.AddTwoLines2(cursor2.GetString("quantity") & " " & cursor2.GetString("unit"),cursor2.GetString("cancelled_reason") & " ("& cursor3.GetString("product_variant") & " - " & cursor3.GetString("product_description") & ")",cursor2.GetString("transaction_number"))
					Next
				End If
			Else if cursor2.GetString("cancelled_reason") = "NEAR EXPIRY" Then
				cursor3 = connection.ExecQuery("SELECT * FROM near_expiry_table WHERE invoice_no = '"& cursor2.GetString("invoice_no") &"' AND transaction_number = '"& cursor2.GetString("transaction_number") &"'")
				If cursor3.RowCount > 0 Then
					For rows= 0 To cursor3.RowCount - 1
						cursor3.Position = rows
						LVL_LIST.AddTwoLines2(cursor2.GetString("quantity") & " " & cursor2.GetString("unit"),cursor2.GetString("cancelled_reason") & " (EXP DATE :"& cursor3.GetString("date_expired") & ")",cursor2.GetString("transaction_number"))
					Next
				End If
			Else
				LVL_LIST.AddTwoLines2(cursor2.GetString("quantity") & " " & cursor2.GetString("unit"),cursor2.GetString("cancelled_reason"),cursor2.GetString("transaction_number"))
			End If
			
		Next
	End If
	EDITTEXT_QUANTITY.Text = ""
End Sub
Sub LVL_LIST_ItemClick (Position As Int, Value As Object)
	cursor3 = connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' And product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"' and transaction_number = '"&Value&"'")
	If cursor3.RowCount > 0 Then
		For row = 0 To cursor3.RowCount - 1
			cursor3.Position = row
			Msgbox2Async("Item Number : " & cursor3.GetString("transaction_number") & CRLF _
				& "Unit : " & cursor3.GetString("unit") & CRLF _
				& "Quantity : " & cursor3.GetString("quantity") & CRLF _
				& "Total Pieces : " & cursor3.GetString("total_pieces") & CRLF _
				, "Option", "EDIT", "CANCEL", "DELETE", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
					Dim bg As ColorDrawable
					bg.Initialize2(Colors.RGB(0,167,255), 5, 0, Colors.LightGray)
					BUTTON_SAVE.Background = bg
					BUTTON_SAVE.Text = " Edit"
					BUTTON_CANCEL.Visible = True
					Sleep(0)
				
				
					EDITTEXT_QUANTITY.Text = cursor3.GetString("quantity")
					Sleep(0)
					CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf(cursor3.GetString("unit"))
					Sleep(0)
					CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.IndexOf(cursor3.GetString("cancelled_reason"))
					CMB_REASON.cmbBox.Enabled = False
					item_number = cursor3.GetString("transaction_number")

					PANEL_LIST.SetVisibleAnimated(300,False)
					PANEL_LIST.BringToFront
					Dim bg2 As ColorDrawable
					bg2.Initialize2(Colors.White,5,0,Colors.Black)
					BUTTON_LIST.Background = bg2
					BUTTON_LIST.TextColor = Colors.RGB(82,169,255)
				
					cancelled_trigger = 0
					cancelled_id = cursor3.GetString("cancelled_id")
					Sleep(0)
					GET_WRONGSERVED_DETAILS
					
					EDITTEXT_QUANTITY.SelectAll
					Sleep(0)
					CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
				
			Else if Result = DialogResponse.NEGATIVE Then
				Msgbox2Async("Are you sure you want to delete this item?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					connection.ExecNonQuery("DELETE FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' And product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"' and transaction_number = '"&Value&"'")
					ToastMessageShow("Deleted Successfully", True)
					Sleep(0)
					connection.ExecNonQuery("DELETE FROM wrong_served_table WHERE cancelled_id = '"& cursor3.GetString("cancelled_id")&"'")
					Sleep(0)
					connection.ExecNonQuery("DELETE FROM near_expiry_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' And product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"' and transaction_number = '"&Value&"'")
					ToastMessageShow("Deleted Successfully", True)
					Sleep(0)
					LOAD_LIST
					Sleep(0)
					GET_CANCELLED_COUNT
				End If
			End If

		Next
	End If
End Sub
Sub GET_TOTAL_CANCELLED
	cursor7 = connection.ExecQuery("SELECT sum(total_pieces) as 'total_cancelled' FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' And product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
	If cursor7.RowCount > 0 Then
		For rowq = 0 To cursor7.RowCount - 1
			cursor7.Position = rowq
			If cursor7.GetString("total_cancelled") = Null Or cursor7.GetString("total_cancelled") = "" Then
				total_cancelled = 0
			Else
				total_cancelled = cursor7.GetString("total_cancelled")
			End If
		Next
	Else
		total_cancelled = 0
	End If
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
			Log(1)
			GET_TOTAL_CANCELLED
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
				total_cancelled = total_cancelled + total_pieces
				If total_cancelled > total_invoice Then
					Msgbox2Async("The total pieces you cancelling is OVER to the total pieces that ordered in this invoice.", "Over Cancelled!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Else
					cursor9 = connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as 'transaction_number' from cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
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
				
					If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("WRONG SERVED") Then
						cancelled_id = LABEL_LOAD_INVOICE_NO.Text&transaction_number&LOGIN_MODULE.tab_id
						INPUT_WRONGSERVED
					Else If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("SHORT RETURN") Or _
					CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("DAMAGE") Then
						cancelled_id = LABEL_LOAD_INVOICE_NO.Text&transaction_number&LOGIN_MODULE.username
					Else If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("NEAR EXPIRY") Then
						cancelled_id = "-"
						INPUT_EXPIRY
						wrong_trigger = 0
					Else 
						cancelled_id = "-"
						wrong_trigger = 0
					End If
					Sleep(100)
					
					If wrong_trigger = 0 Then
						Dim query As String = "INSERT INTO cancelled_invoice_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
						connection.ExecNonQuery2(query,Array As String(customer_id,LABEL_LOAD_CUSTOMER_NAME.Text,LABEL_LOAD_INVOICE_NO.Text,LABEL_LOAD_INVOICE_DATE.Text,transaction_number, _
						principal_id, LABEL_LOAD_PRINCIPAL.Text, product_id, LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text, CMB_UNIT.cmbBox.SelectedItem, _
						EDITTEXT_QUANTITY.Text, total_pieces, scan_code, reason, CMB_REASON.cmbBox.SelectedItem, cancelled_id ,DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), LOGIN_MODULE.username, LOGIN_MODULE.tab_id))
						Sleep(0)
						ToastMessageShow("Product Cancelled", False)
						GET_CANCELLED_COUNT
						Sleep(0)
						EDITTEXT_QUANTITY.Text = ""
						CMB_REASON.SelectedIndex = -1
						OpenSpinner(CMB_REASON.cmbBox)
						Sleep(0)
						CLEAR_WRONGSERVED
						CLEAR_EXPIRATION
					Else If wrong_trigger = 1 Then
						Msgbox2Async("The wrong served product you inputted has no unit of " & CMB_UNIT.cmbBox.SelectedItem & ", Cannot proceed." , "Warning!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					End If
				End If
			Else
				Msgbox2Async("Are you sure you want to update this item?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					If Result = DialogResponse.POSITIVE Then
						If CMB_REASON.cmbBox.SelectedIndex = CMB_REASON.cmbBox.IndexOf("WRONG SERVED") Then
							UPDATE_WRONGSERVED
						Else
							wrong_trigger = 0
						End If
						
						If wrong_trigger = 0 Then
							Dim query As String = "UPDATE cancelled_invoice_disc_table SET unit = ?, quantity = ?, total_pieces = ?, user_info = ?, date_registered = ? , time_registered = ? WHERE invoice_no = ? And product_description = ? and transaction_number = ?"
							connection.ExecNonQuery2(query,Array As String(CMB_UNIT.cmbBox.SelectedItem, EDITTEXT_QUANTITY.Text, total_pieces, LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.now), LABEL_LOAD_INVOICE_NO.Text, LABEL_LOAD_DESCRIPTION.Text, item_number))
							Sleep(0)
							Dim query As String = "UPDATE near_expiry_table SET unit = ?, quantity = ?, total_pieces = ?, user_info = ?, date_registered = ? , time_registered = ? WHERE invoice_no = ? And product_description = ? and transaction_number = ?"
							connection.ExecNonQuery2(query,Array As String(CMB_UNIT.cmbBox.SelectedItem, EDITTEXT_QUANTITY.Text, total_pieces, LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.now), LABEL_LOAD_INVOICE_NO.Text, LABEL_LOAD_DESCRIPTION.Text, item_number))
							Sleep(0)
							ToastMessageShow("Transaction Updated", False)
							CLEAR
							Sleep(0)
							LOAD_LIST
							CLEAR_EXPIRATION
							CLEAR_WRONGSERVED
						Else If wrong_trigger = 1 Then
							Msgbox2Async("The wrong served product you inputted has no unit of " & CMB_UNIT.cmbBox.SelectedItem & ", Cannot proceed." , "Warning!", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						End If
						
					End If
				End If
			End If

		End If
	Else
		Msgbox2Async("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
End Sub
Sub GET_CANCELLED_COUNT
	cursor8 = connection.ExecQuery("SELECT COUNT(product_id) as 'count_cancelled' FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"' And product_description ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
	If cursor8.RowCount > 0 Then
		For rowq = 0 To cursor8.RowCount - 1
			cursor8.Position = rowq
		BUTTON_LIST.Text = " Cancelled("&cursor8.GetString("count_cancelled")&")"
		Next
	Else
		BUTTON_LIST.Text = " Cancelled(0)"
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
	PANEL_LIST.SetVisibleAnimated(300,False)
	PANEL_LIST.BringToFront
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.White,5,0,Colors.Black)
	BUTTON_LIST.Background = bg
	BUTTON_LIST.TextColor = Colors.RGB(82,169,255)
	cancelled_trigger = 0
End Sub
Sub BUTTON_CANCEL_Click
	CLEAR
End Sub
Sub CMB_REASON_SelectedIndexChanged (Index As Int)
	If CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.IndexOf("WRONG SERVED") Then
		PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, True)
		PANEL_BG_WRONGSERVED.BringToFront
		CLEAR_WRONGSERVED
		CLEAR_EXPIRATION
	Else If CMB_REASON.SelectedIndex = CMB_REASON.cmbBox.IndexOf("NEAR EXPIRY") Then
		PANEL_BG_EXPIRATION.SetVisibleAnimated(300, True)
		PANEL_BG_EXPIRATION.BringToFront
		CLEAR_WRONGSERVED
		CLEAR_EXPIRATION
		If default_reading = "BOTH" Or default_reading = "Expiration Date" Then
			OpenLabel(LABEL_LOAD_EXPIRATION)
		Else If default_reading = "Manufacturing Date" Then
			OpenLabel(LABEL_LOAD_MANUFACTURED)
		Else
		
		End If
	Else
		CMB_UNIT.SelectedIndex = -1
		OpenSpinner(CMB_UNIT.cmbBox)
		CLEAR_EXPIRATION
		CLEAR_WRONGSERVED
	End If
End Sub
Sub CMB_UNIT_SelectedIndexChanged (Index As Int)
	EDITTEXT_QUANTITY.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
	EDITTEXT_QUANTITY.SelectAll
End Sub

Sub INPUT_WRONGSERVED
	If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
		wrong_total_pieces = wrong_caseper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
		wrong_total_pieces = wrong_pcsper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
		wrong_total_pieces = wrong_dozper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
		wrong_total_pieces = wrong_boxper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
		wrong_total_pieces = wrong_bagper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
		wrong_total_pieces = wrong_packper * EDITTEXT_QUANTITY.text
	End If
	Log (wrong_total_pieces)
	If wrong_total_pieces > 0 Then
		Log("open")
		wrong_trigger = 0
		Dim query As String = "INSERT INTO wrong_served_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
		connection.ExecNonQuery2(query,Array As String( LABEL_LOAD_INVOICE_NO.Text, cancelled_id , _
		wrong_principal_id, LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text, product_id, LABEL_LOAD_WRONGSERVED_VARIANT.Text, LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text, CMB_UNIT.cmbBox.SelectedItem, _
		EDITTEXT_QUANTITY.Text, wrong_total_pieces, wrong_scan_code, wrong_reason,DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), LOGIN_MODULE.username, LOGIN_MODULE.tab_id))
	Else
		Log("close")
		wrong_trigger = 1
	End If
End Sub
Sub BUTTON_WRONGSERVED_EXIT_Click
	CLEAR_WRONGSERVED
	PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, False)
	Sleep(0)
	CMB_REASON.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_REASON.cmbBox)
	ToastMessageShow("Wrong served cancel", False)
End Sub
Sub BUTTON_CONFIRM_Click
	If LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = "-" Then
		ToastMessageShow("Please input a product first.", False)
	Else
		PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, False)
		Sleep(0)
		CMB_UNIT.SelectedIndex = -1
		OpenSpinner(CMB_UNIT.cmbBox)
		BUTTON_WRONGSERVED.Visible = True
	End If
End Sub
Sub CLEAR_WRONGSERVED
	LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = "-"
	LABEL_LOAD_WRONGSERVED_VARIANT.Text = "-"
	LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = "-"
	BUTTON_WRONGSERVED.Visible = False
End Sub
Sub BUTTON_WRONGSERVED_Click
	PANEL_BG_WRONGSERVED.SetVisibleAnimated(300, True)
	PANEL_BG_WRONGSERVED.BringToFront
End Sub
Sub WRONGSERVED_MANUAL
	Sleep(0)
	SearchTemplate2.CustomListView1.Clear
	Dialog.Title = "Find Product"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' ORDER BY product_desc ASC")
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		Items.Add(cursor2.GetString("product_desc"))
	Next
	SearchTemplate2.SetItems(Items)
End Sub
Sub BUTTON_WRONGSERVED_MANUAL_Click
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
#Region Find Product
			cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&SearchTemplate2.SelectedItem&"'")
			For qrow = 0 To cursor3.RowCount - 1
				cursor3.Position = qrow
				LABEL_LOAD_WRONGSERVED_VARIANT.Text = cursor3.GetString("product_variant")
				LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = cursor3.GetString("product_desc")
				wrong_principal_id = cursor3.GetString("principal_id")
				wrong_product_id = cursor3.GetString("product_id")
			
				wrong_caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
				wrong_pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
				wrong_dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
				wrong_boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
				wrong_bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
				wrong_packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
			Next
			cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & wrong_principal_id & "'")
			If cursor6.RowCount > 0 Then
				For row = 0 To cursor6.RowCount - 1
					cursor6.Position = row
					LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = cursor6.Getstring("principal_name")
				Next
				wrong_reason = ls.Get(Result2)
				wrong_scan_code = "N/A"
			End If
#End Region		
		End If
	End If
End Sub
Sub GET_WRONGSERVED_DETAILS
	Dim p_id As String
	cursor7 = connection.ExecQuery("SELECT * FROM wrong_served_table WHERE cancelled_id = '"& cancelled_id &"'")
	If cursor7.RowCount > 0 Then
		For rows= 0 To cursor7.RowCount - 1
			cursor7.Position = rows
			p_id = cursor7.GetString("product_id")
		Next
		cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_id ='"&p_id&"'")
		For qrow = 0 To cursor3.RowCount - 1
			cursor3.Position = qrow
			LABEL_LOAD_WRONGSERVED_VARIANT.Text = cursor3.GetString("product_variant")
			LABEL_LOAD_WRONGSERVED_DESCRIPTION.Text = cursor3.GetString("product_desc")
			wrong_principal_id = cursor3.GetString("principal_id")
			wrong_product_id = cursor3.GetString("product_id")
			
			wrong_caseper = cursor3.GetString("CASE_UNIT_PER_PCS")
			wrong_pcsper = cursor3.GetString("PCS_UNIT_PER_PCS")
			wrong_dozper = cursor3.GetString("DOZ_UNIT_PER_PCS")
			wrong_boxper = cursor3.GetString("BOX_UNIT_PER_PCS")
			wrong_bagper = cursor3.GetString("BAG_UNIT_PER_PCS")
			wrong_packper = cursor3.GetString("PACK_UNIT_PER_PCS")
		
		Next
		cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & wrong_principal_id & "'")
		If cursor6.RowCount > 0 Then
			For row = 0 To cursor6.RowCount - 1
				cursor6.Position = row
				LABEL_LOAD_WRONGSERVED_PRINCIPAL.Text = cursor6.Getstring("principal_name")
			Next
		End If
	End If
End Sub
Sub UPDATE_WRONGSERVED
	If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
		wrong_total_pieces = wrong_caseper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
		wrong_total_pieces = wrong_pcsper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
		wrong_total_pieces = wrong_dozper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
		wrong_total_pieces = wrong_boxper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
		wrong_total_pieces = wrong_bagper * EDITTEXT_QUANTITY.text
	Else if CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
		wrong_total_pieces = wrong_packper * EDITTEXT_QUANTITY.text
	End If
	Log (wrong_total_pieces)
	If wrong_total_pieces > 0 Then
		Log("open")
		wrong_trigger = 0
		Dim query As String = "UPDATE wrong_served_table SET unit = ?, quantity = ?, total_pieces = ? , user_info = ?, date_registered = ? , time_registered = ? WHERE cancelled_id = ?"
		connection.ExecNonQuery2(query,Array As String(CMB_UNIT.cmbBox.SelectedItem, EDITTEXT_QUANTITY.Text, wrong_total_pieces, LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.now), cancelled_id))
		Sleep(0)
		ToastMessageShow("Transaction Updated", False)
	Else
		Log("close")
		wrong_trigger = 1
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
	CLEAR_EXPIRATION
	PANEL_BG_EXPIRATION.SetVisibleAnimated(300, False)
	Sleep(0)
	CMB_REASON.SelectedIndex = -1
	Sleep(0)
	OpenSpinner(CMB_REASON.cmbBox)
	ToastMessageShow("Near expiry cancel", False)
End Sub

Sub BUTTON_EXPIRATION_CONFIRM_Click
	If LABEL_LOAD_EXPIRATION.Text = "-" Or LABEL_LOAD_MANUFACTURED.Text = "-" Then
		ToastMessageShow("Please input a expiration first.", False)
	Else
		INPUT_NEAR_EXPIRY
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
	Dim query As String = "INSERT INTO near_expiry_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
		connection.ExecNonQuery2(query,Array As String(LABEL_LOAD_INVOICE_NO.Text,transaction_number,principal_id,LABEL_LOAD_PRINCIPAL.Text, _
		product_id,LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text,CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QUANTITY.Text,total_pieces, _
		monthexp,yearexp,LABEL_LOAD_EXPIRATION.Text,monthmfg,yearmfg,LABEL_LOAD_MANUFACTURED.Text, scan_code, reason,  _
		DateTime.Date(DateTime.now),DateTime.Time(DateTime.Now),LOGIN_MODULE.username,LOGIN_MODULE.tab_id))
		Sleep(0)
		ToastMessageShow("Product Expiration Added Succesfully",False)
End Sub

Sub BUTTON_UPLOAD_Click
	cursor9 = connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	If cursor9.RowCount > 0 Then
		Msgbox2Async("Are you sure you want to upload this cancelled invoice?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_CANCELLED_INVOICE_DISC
		Else
		
		End If
	Else
		Msgbox2Async("There's nothing to upload in this invoice.", "Empty Upload", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		
	End If
End Sub

Sub DELETE_CANCELLED_INVOICE_DISC
	PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)
	PANEL_BG_MSGBOX.BringToFront
	LABEL_HEADER_TEXT.Text = "Uploading Data"
	LABEL_MSGBOX2.Text = "Fetching Data..."
	LABEL_MSGBOX1.Text = "Loading, Please wait..."
	Dim cmd As DBCommand = CreateCommand("delete_cancelled_invoice_disc", Array(LABEL_LOAD_INVOICE_NO.Text))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_CANCELLED_INVOICE_DISC
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
			DELETE_CANCELLED_INVOICE_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_CANCELLED_INVOICE_DISC
	error_trigger = 0
	cursor6 = connection.ExecQuery("SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	If cursor6.RowCount > 0 Then
		For i = 0 To cursor6.RowCount - 1
			cursor6.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_cancelled_invoice_disc", Array As String(cursor6.GetString("customer_id"),cursor6.GetString("customer_name"), cursor6.GetString("invoice_no"),cursor6.GetString("invoice_date"), _
			cursor6.GetString("transaction_number"),cursor6.GetString("principal_id"),cursor6.GetString("principal_name"),cursor6.GetString("product_id"),cursor6.GetString("product_variant"), _
			cursor6.GetString("product_description"),cursor6.GetString("unit"),cursor6.GetString("quantity"),cursor6.GetString("total_pieces"),cursor6.GetString("scan_code"),cursor6.GetString("input_reason"), _
			cursor6.GetString("cancelled_reason"),cursor6.GetString("cancelled_id"),cursor6.GetString("date_registered"),cursor6.GetString("time_registered"),cursor6.GetString("user_info"),cursor6.GetString("tab_id")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor6.GetString("product_description")
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
	End If
	Sleep(1000)
	If error_trigger = 0 Then
		DELETE_CANCELLED_WRONGSERVED
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Updating Failed", False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_CANCELLED_INVOICE_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_CANCELLED_WRONGSERVED
	Dim cmd As DBCommand = CreateCommand("delete_cancelled_wrong_served", Array(LABEL_LOAD_INVOICE_NO.Text))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_CANCELLED_WRONGSERVED
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
			DELETE_CANCELLED_INVOICE_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_CANCELLED_WRONGSERVED
	error_trigger = 0
	cursor4 = connection.ExecQuery("SELECT * FROM wrong_served_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	If cursor4.RowCount > 0 Then
		For i = 0 To cursor4.RowCount - 1
			cursor4.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_cancelled_wrong_served", Array As String(cursor4.GetString("invoice_no"),cursor4.GetString("cancelled_id"), _
			cursor4.GetString("principal_id"),cursor4.GetString("principal_name"),cursor4.GetString("product_id"),cursor4.GetString("product_variant"), _
			cursor4.GetString("product_description"),cursor4.GetString("unit"),cursor4.GetString("quantity"),cursor4.GetString("total_pieces"),cursor4.GetString("scan_code"),cursor4.GetString("input_reason"), _
			cursor4.GetString("date_registered"),cursor4.GetString("time_registered"),cursor4.GetString("user_info"),cursor4.GetString("tab_id")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor4.GetString("product_description")
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
			DELETE_CANCELLED_NEAREXPIRY
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				DELETE_CANCELLED_INVOICE_DISC
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
	Else
		DELETE_CANCELLED_NEAREXPIRY
	End If
	Sleep(1000)
End Sub
Sub DELETE_CANCELLED_NEAREXPIRY
	Dim cmd As DBCommand = CreateCommand("delete_near_expiry", Array(LABEL_LOAD_INVOICE_NO.Text))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_CANCELLED_NEAREXPIRY
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
			DELETE_CANCELLED_INVOICE_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_CANCELLED_NEAREXPIRY
	error_trigger = 0
	cursor2 = connection.ExecQuery("SELECT * FROM near_expiry_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	If cursor2.RowCount > 0 Then
		For i = 0 To cursor2.RowCount - 1
			cursor2.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_cancelled_near_expiry", Array As String(cursor2.GetString("invoice_no"),cursor2.GetString("transaction_number"), _
			cursor2.GetString("principal_id"),cursor2.GetString("principal_name"),cursor2.GetString("product_id"),cursor2.GetString("product_variant"), _
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
			LOG_INVOICE_UPLOAD
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
			Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				DELETE_CANCELLED_INVOICE_DISC
			Else
				PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
				ToastMessageShow("Updating Failed", False)
			End If
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		LABEL_MSGBOX2.Text = "Uploaded Successfully..."
		ToastMessageShow("Uploaded Successfully", False)
		LOG_INVOICE_UPLOAD
		Sleep(0)
		CLEAR_INVOICE
		Sleep(0)
		OPEN_INVOICE

	End If
End Sub

Sub LOG_INVOICE_UPLOAD
	connection.ExecNonQuery("DELETE FROM cancelled_upload_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	Sleep(0)
	Dim query As String = "INSERT INTO cancelled_upload_table VALUES (?,?,?,?,?)"
	connection.ExecNonQuery2(query,Array As String(LABEL_LOAD_INVOICE_NO.Text,DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), LOGIN_MODULE.username, LOGIN_MODULE.tab_id))
End Sub

Sub LOAD_INVOICE_UPLOAD
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LVL_INVOICE.Background = bg
	LVL_INVOICE.Clear
	Sleep(0)
	LVL_INVOICE.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LVL_INVOICE.TwoLinesLayout.Label.TextSize = 20
	LVL_INVOICE.TwoLinesLayout.SecondLabel.Top = -1%y
	LVL_INVOICE.TwoLinesLayout.label.Height = 6%y
	LVL_INVOICE.TwoLinesLayout.Label.TextColor = Colors.Black
	LVL_INVOICE.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LVL_INVOICE.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LVL_INVOICE.TwoLinesLayout.SecondLabel.Top = 4.3%y
	LVL_INVOICE.TwoLinesLayout.SecondLabel.TextSize = 14
	LVL_INVOICE.TwoLinesLayout.SecondLabel.Height = 3%y
	LVL_INVOICE.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LVL_INVOICE.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LVL_INVOICE.TwoLinesLayout.ItemHeight = 8%y
	Sleep(0)
	For i = 0 To Invoice.Size - 1
		cursor3 = connection.ExecQuery("SELECT a.*,b.upload_date,b.upload_time, b.user_info as 'upload_user' FROM (SELECT * FROM cancelled_invoice_disc_table WHERE invoice_no = '"&Invoice.Get(i)&"' GROUP BY invoice_no) as a " & _
		"LEFT JOIN cancelled_upload_table As b " & _
		"ON a.invoice_no = b.invoice_no")
		If cursor3.RowCount > 0 Then
			For rows= 0 To cursor3.RowCount - 1
				cursor3.Position = rows
				If cursor3.GetString("upload_date") = Null Or cursor3.GetString("upload_date") = "" Then
					LVL_INVOICE.AddTwoLines2(cursor3.GetString("invoice_no") & " - SAVED" ,"User : " & cursor3.GetString("user_info")  ,cursor3.GetString("invoice_no"))
				Else
					LVL_INVOICE.AddTwoLines2(cursor3.GetString("invoice_no") & " - UPLOADED","Upload by : " & cursor3.GetString("upload_user") & " Date Time : " & cursor3.GetString("upload_date") & " " & cursor3.GetString("upload_time"),cursor3.GetString("invoice_no"))
				End If
			Next
		End If
	Next
	PANEL_BG_INVOICE.SetVisibleAnimated(300,True)
	PANEL_BG_INVOICE.BringToFront
End Sub

Sub BUTTON_INVOICE_EXIT_Click
	PANEL_BG_INVOICE.SetVisibleAnimated(300,False)
	
End Sub
Sub BUTTON_CANCELLED_ALL_Click
	Msgbox2Async("Are you sure you to cancelled all invoice?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Dim ls As List
		ls.Initialize
		ls.Add("NOT ORDERED")
		ls.Add("OVERSTOCK")
		ls.Add("REFUSAL")
		ls.Add("CLOSED STORE")
		ls.Add("DOUBLE ENCODE")
		ls.Add("NO CASH OR CHEQUE AVAILABLE")
		InputListAsync(ls, "Choose reason", -1, True) 'show list with paired devices
		Wait For InputList_Result (Result2 As Int)
		If Result2 <> DialogResponse.CANCEL Then
			scan_code = "N/A"
			reason = "ALL CANCELLED"
			cancelled_reason = ls.Get(Result2)
			cancelled_id = "-"
			Sleep(0)
			AUTO_FILL
		End If
	Else
		
	End If
End Sub
Sub AUTO_FILL
	Dim principal_name As String
	connection.ExecNonQuery("DELETE FROM cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	ProgressDialogShow2("Auto Filling...", False)
	cursor9 = connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as 'transaction_number' from cancelled_invoice_disc_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	If cursor9.RowCount > 0 Then
		For i = 0 To cursor9.RowCount - 1
			cursor9.Position = i
			If cursor9.GetString("transaction_number") = Null Or cursor9.GetString("transaction_number") = "" Then
				transaction_number = 1
			Else
				transaction_number = cursor9.GetString("transaction_number")
			End If
		Next
	End If
	
	cursor4 = connection.ExecQuery("SELECT * FROM cancelled_invoice_ref_table WHERE invoice_no = '"&LABEL_LOAD_INVOICE_NO.Text&"'")
	If cursor4.RowCount > 0 Then
		Sleep(0)
		For ia = 0 To cursor4.RowCount - 1
			Sleep(0)
			cursor4.Position = ia
			transaction_number = transaction_number + 1
			
			cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&cursor4.GetString("product_description")&"'")
			For qrow = 0 To cursor3.RowCount - 1
				cursor3.Position = qrow
				principal_id = cursor3.GetString("principal_id")
				product_id = cursor3.GetString("product_id")
		
			Next
			cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
			If cursor6.RowCount > 0 Then
				For row = 0 To cursor6.RowCount - 1
					cursor6.Position = row
					principal_name = cursor6.Getstring("principal_name")
				Next
			End If
			Sleep(0)
			
			Dim query As String = "INSERT INTO cancelled_invoice_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			connection.ExecNonQuery2(query,Array As String(customer_id,LABEL_LOAD_CUSTOMER_NAME.Text,LABEL_LOAD_INVOICE_NO.Text,LABEL_LOAD_INVOICE_DATE.Text,transaction_number, _
						principal_id, principal_name, product_id, cursor4.GetString("product_variant"), cursor4.GetString("product_description"), cursor4.GetString("unit"), _
						cursor4.GetString("quantity"), cursor4.GetString("total_pieces"), scan_code, reason, cancelled_reason, cancelled_id ,DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now), LOGIN_MODULE.username, LOGIN_MODULE.tab_id))
		Next
		ToastMessageShow("Auto Filling Successfull", False)
		Sleep(0)
		LOAD_INVOICE_PRODUCT
		Sleep(1000)
		ProgressDialogHide()
	Else
		
	End If
End Sub

Sub CLEAR_INVOICE
	LABEL_LOAD_CUSTOMER_NAME.Text = "-"
	LABEL_LOAD_INVOICE_DATE.Text = "-"
	LABEL_LOAD_INVOICE_NO.Text = "-"
	BUTTON_UPLOAD.Visible = False
	BUTTON_CANCELLED_ALL.Visible = False
	Sleep(0)
	LOAD_INVOICE_PRODUCT
	Sleep(0)
	
End Sub

Sub EDITTEXT_QUANTITY_EnterPressed
	OpenButton(BUTTON_SAVE)
End Sub

Sub OpenButton(se As Button)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		Return True
	End If
End Sub