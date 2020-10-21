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
	
	'	calcu
	Dim ProgPath = File.DirRootExternal & "/TapeCalc" As String
	Dim ScaleAuto As Double
	Dim Texts(8) As String
	Dim LanguageID As String
'	
	Dim sVal = "" As String
	Dim Val = 0 As Double
	Dim Op0 = "" As String
	Dim Result1 = 0 As Double
	Dim Txt = "" As String
	Dim New1 = 0 As Int
	
	'String
	Dim old_doc_no As String
	
	Private cartBitmap As Bitmap
	Private uploadBitmap As Bitmap
		
	'bluetooth
	Dim serial1 As Serial
	Dim AStream As AsyncStreams
	Dim Ts As Timer
	
		
	'product
	Public principal_id As String
	Public product_id As String
	
	Dim reason As String
	Dim input_type As String
	Dim caseper As String
	Dim pcsper As String
	Dim dozper As String
	Dim boxper As String
	Dim bagper As String
	Dim packper As String
	Dim total_pieces As Int
	Dim price As Int
	Dim total_price As Int
	Dim transaction_number As String
	
	Dim scan_code As String
	
	Dim total_order As Int
	Dim total_input As Int
	
	Dim lifespan_month As String
	Dim lifespan_year As String
	
	Dim default_reading As String
	
	Dim monthexp As String
	Dim yearexp As String
	
	Dim monthmfg As String
	Dim yearmfg As String
	Dim error_trigger As String
	Dim security_trigger As String
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim CTRL As IME
	
	Dim phone As Phone
	
	Private cvs As B4XCanvas
	Private xui As XUI
	Private NameColumn(5) As B4XTableColumn
	Private NameColumn2(5) As B4XTableColumn
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
	Private XSelections As B4XTableSelections
	Private TABLE_PURCHASE_ORDER As B4XTable
	
	'	calcu
	Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9 As Button
	Dim btnBack, btnClr, btnExit As Button
	Dim lblPaperRoll As Label
	Dim scvPaperRoll As ScrollView
	Dim pnlKeyboard As Panel
	Dim stu As StringUtils
	Private LABEL_LOAD_ANSWER As Label
	Private PANEL_BG_CALCU As Panel
	
	
	'bluetooth
	Dim ScannerMacAddress As String
	Dim ScannerOnceConnected As Boolean
	
	'
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private DateTemplate As B4XDateTemplate
	Private DateTemplate2 As B4XDateTemplate
	Private SearchTemplate As B4XSearchTemplate

	Private LABEL_LOAD_PRINCIPAL As Label
	Private PANEL_BG_DELIVERY As Panel
	Private BUTTON_OKAY As Button
	Private LISTVIEW_DR As ListView
	Private EDITTEXT_DOC_NO As EditText
	Private EDITTEXT_DRIVER As EditText
	Private EDITTEXT_TRUCKING As EditText
	Private EDITTEXT_PLATE As EditText
	Private EDITTEXT_TRUCKTYPE As EditText
	Private BUTTON_SAVE As Button
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_DESCRIPTION As Label
	Private CMB_UNIT As B4XComboBox
	Private EDITTEXT_QUANTITY As EditText
	Private LABEL_LOAD_ORDER As Label
	Private LABEL_LOAD_INPUT As Label
	Private LABEL_LOAD_BALANCE As Label
	Private CMB_INVOICE As B4XComboBox
	Private PANEL_BG_INPUT As Panel
	Private LABEL_LOAD_MANUFACTURED As Label
	Private LABEL_LOAD_EXPIRATION As Label
	Private LVL_LIST As ListView
	Private BUTTON_CANCEL As Button
	Private BUTTON_ADD As Button
	Private PANEL_BG_SECURITY As Panel
	Private EDITTEXT_PASSWORD As EditText
	Private CMB_ACCOUNT As B4XComboBox
	Private PANEL_BG_INVOICE As Panel
	Private TABLE_INVOICE As B4XTable
	Private LABEL_LOAD_TOTALAMT As Label
	Private LABEL_LOAD_TOTALCASE As Label
	Private CMB_INV As B4XComboBox
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_MSGBOX1 As Label
	Private LABEL_MSGBOX2 As Label
	Private LABEL_HEADER_TEXT As Label
	Private PANEL_BG_EXPIRATION As Panel
	Private LABEL_LOAD_MANUFACTURING As Label
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
	Activity.LoadLayout("receiving2")
	
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	uploadBitmap = LoadBitmap(File.DirAssets, "upload.png")
'	plusBitmap = LoadBitmap(File.DirAssets, "add.png")

	ToolbarHelper.Initialize
	ToolbarHelper.Icon = BitmapToBitmapDrawable(LoadBitmap(File.DirAssets, "LOGO_3D.png"))
	ToolbarHelper.ShowUpIndicator = True 'set to true to show the up arrow
	Dim bd As BitmapDrawable
	bd.Initialize(LoadBitmap(File.DirAssets, "back.png"))
	ToolbarHelper.UpIndicatorDrawable =  bd
	ACToolBarLight1.InitMenuListener
	ACToolBarLight1.SubTitle = "PO NO : " & RECEIVING_MODULE.purchase_order_no
	
	LABEL_LOAD_PRINCIPAL.Text = RECEIVING_MODULE.principal_name
	
	Dim jo As JavaObject = ACToolBarLight1
	jo.RunMethod("setContentInsetStartWithNavigation", Array(5dip))
	jo.RunMethod("setTitleMarginStart", Array(10dip))

	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", False)
	End If
	
	phone.SetScreenOrientation(0)
	
	Dim p As B4XView = xui.CreatePanel("")
	p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)
	cvs.Initialize(p)
	
	'blueetooth
	serial1.Initialize("Serial")
	Ts.Initialize("Timer", 2000)
	
	Base = Activity
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
	
	Sleep(0)
	Dim Ref As Reflector
	Ref.Target = EDITTEXT_DRIVER ' The text field being referenced
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	Ref.Target = EDITTEXT_PLATE
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	Ref.Target = EDITTEXT_TRUCKING
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	Ref.Target = EDITTEXT_TRUCKTYPE
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	Ref.Target = EDITTEXT_DOC_NO
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	Ref.Target = EDITTEXT_QUANTITY
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	Ref.Target = EDITTEXT_PASSWORD
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_PASSWORD.Background = bg
	
	VIEW_DR
	Sleep(0)
	LOAD_PURCHASE_HEADER
	Sleep(0)
	LOAD_PURCHASE_ORDER
	Sleep(0)
	INPUT_MANUAL
	Sleep(0)
	LOAD_INVOICE_HEADER

End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "cart", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
	Dim item2 As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 0, "upload", Null)
	item2.ShowAsAction = item2.SHOW_AS_ACTION_ALWAYS
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
Sub OpenLabel(se As Label)
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
	StartActivity(RECEIVING_MODULE)
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
	If Item.Title = "cart" Then
	Log("Resuming...")
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_connecting.png")
	UpdateIcon("cart", cartBitmap)
	ShowPairedDevices
	Else If Item.Title = "upload" Then
		Msgbox2Async("Are you sure you want to upload this transaction?", "Upload Receiving", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RECEIVING_DISC
		End If
	End If
End Sub

Sub LOAD_PURCHASE_HEADER
	NameColumn(0)=TABLE_PURCHASE_ORDER.AddColumn("Product Variant", TABLE_PURCHASE_ORDER.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_PURCHASE_ORDER.AddColumn("Product Description", TABLE_PURCHASE_ORDER.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_PURCHASE_ORDER.AddColumn("Total Case", TABLE_PURCHASE_ORDER.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_PURCHASE_ORDER.AddColumn("Total Received", TABLE_PURCHASE_ORDER.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_PURCHASE_ORDER.AddColumn("Balance", TABLE_PURCHASE_ORDER.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_PURCHASE_ORDER
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	Dim total_received As Int
	
	Dim total_case_order As String
	Dim total_case_received As String
	Dim balance As String
	cursor10 = connection.ExecQuery("SELECT a.*, b.total_pcs_received , c.CASE_UNIT_PER_PCS FROM " & _
	"(Select *, sum(total_pieces) As 'total_pcs_order' FROM purchase_order_ref_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' GROUP BY po_doc_no, product_description) as a " & _
	"LEFT JOIN " & _
	"(Select *, sum(total_pieces) As 'total_pcs_received' FROM receiving_disc_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' GROUP BY po_doc_no, product_description) as b " & _
	"ON a.po_doc_no = b.po_doc_no And a.product_description = b.product_description " & _
	"LEFT JOIN " & _
	"(Select * FROM product_table) As c " & _
	"ON a.product_description = c.product_desc")
	If cursor10.RowCount > 0 Then
		For ia = 0 To cursor10.RowCount - 1
			Sleep(10)
			cursor10.Position = ia
			If cursor10.GetString("total_pcs_received") = Null Or cursor10.GetString("total_pcs_received") = "" Then
				total_received = 0
				total_case_received = 0
			Else
				total_received = cursor10.GetString("total_pcs_received")
				total_case_received = Number.Format3((total_received / cursor10.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
			End If
			
			total_case_order = Number.Format3((cursor10.GetString("total_pcs_order") / cursor10.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
			balance = Number.Format3(((total_received - cursor10.GetString("total_pcs_order")) / cursor10.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
		
		
			Dim row(5) As Object
			row(0) = cursor10.GetString("product_variant")
			row(1) = cursor10.GetString("product_description")
			Log(total_case_order.SubString(total_case_order.IndexOf(".")+1))
			If total_case_order.SubString(total_case_order.IndexOf(".")+1) > 0 Then
				row(2) = Number.Format3((cursor10.GetString("total_pcs_order") / cursor10.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
			Else
				row(2) = Number.Format3((cursor10.GetString("total_pcs_order") / cursor10.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15)
			End If
			If total_case_received.SubString(total_case_received.IndexOf(".")+1) > 0 Then
				row(3) = Number.Format3((total_received / cursor10.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
			Else
				row(3) = Number.Format3((total_received / cursor10.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15)
			End If
			If balance.SubString(balance.IndexOf(".")+1) > 0 Then
				row(4) = Number.Format3(((total_received - cursor10.GetString("total_pcs_order")) / cursor10.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
			Else
				row(4) = Number.Format3(((total_received - cursor10.GetString("total_pcs_order")) / cursor10.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15)
			End If
			Data.Add(row)
		Next

		RECEIVING_MODULE.transaction_type = "PURCHASE ORDER"
	Else
		RECEIVING_MODULE.transaction_type = "AUTO SHIP"
	End If
	
	cursor6 = connection.ExecQuery("SELECT a.*, b.CASE_UNIT_PER_PCS FROM" & _
		"(Select *, sum(total_pieces) As total_pcs_received FROM receiving_disc_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' AND product_description " & _
		"Not IN (Select product_description FROM purchase_order_ref_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"') GROUP BY product_id) as a " & _
		"LEFT JOIN " & _
		"product_table As b " & _
		"ON a.product_description = b.product_desc GROUP BY a.product_id")
	If cursor6.RowCount > 0 Then
		For ia = 0 To cursor6.RowCount - 1
			Sleep(10)
			cursor6.Position = ia
				
			If cursor6.GetString("total_pcs_received") = Null Or cursor6.GetString("total_pcs_received") = "" Then
				total_received = 0
				total_case_received = 0
				Log(total_received & " -")
			Else
				total_received = cursor6.GetString("total_pcs_received")
				total_case_received = Number.Format3((total_received / cursor6.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
				Log(total_received & " +")
			End If
				
			If cursor6.GetString("product_description") = Null Or cursor6.GetString("product_description") = "" Then
			Else
				Dim row(5) As Object
				row(0) = cursor6.GetString("product_variant")
				row(1) = cursor6.GetString("product_description")
				row(2) = 0
				If total_case_received.SubString(total_case_received.IndexOf(".")+1) > 0 Then
					row(3) = Number.Format3((total_received / cursor6.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
				Else
					row(3) = Number.Format3((total_received / cursor6.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15)
				End If
				
				balance = Number.Format3(((total_received - 0) / cursor6.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
				If balance.SubString(balance.IndexOf(".")+1) > 0 Then
					row(4) = Number.Format3(((total_received - 0) / cursor6.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
				Else
					row(4) = Number.Format3(((total_received - 0) / cursor6.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15)
				End If
				Data.Add(row)
			End If
		Next
	Else
		
	End If
	
	Sleep(0)
	TABLE_PURCHASE_ORDER.RowHeight = 50dip
	Sleep(100)
	ProgressDialogHide
	Sleep(0)
	TABLE_PURCHASE_ORDER.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_PURCHASE_ORDER)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_PURCHASE_ORDER_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(1),NameColumn(2), NameColumn(3),NameColumn(4))

		Dim MaxWidth As Int
		Dim MaxHeight As Int
		For i = 0 To TABLE_PURCHASE_ORDER.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
'			lbl.SetTextAlignment(Gravity.RIGHT,Gravity.CENTER)
'			MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.Text, lbl.Font).Height + 10dip)
		Next
		
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
		
	Next
	
	For Each Column As B4XTableColumn In Array (NameColumn(2), NameColumn(3),NameColumn(4))
	
		For i = 0 To TABLE_PURCHASE_ORDER.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			lbl.Font = xui.CreateDefaultBoldFont(18)
		Next
	Next
	
	For i = 0 To TABLE_PURCHASE_ORDER.VisibleRowIds.Size - 1
		Dim RowId As Long = TABLE_PURCHASE_ORDER.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(4).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = TABLE_PURCHASE_ORDER.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(4).Id)
			If OtherColumnValue > 0 Then
				clr = xui.Color_Blue
			Else If OtherColumnValue < 0  Then
				clr = xui.Color_Red
			Else
				clr = xui.Color_Green
			End If
			pnl1.GetView(0).SetColorAndBorder(clr, 1dip, Colors.RGB(215,215,215), 0)
			
		End If
	Next
	For Each Column As B4XTableColumn In Array (NameColumn(0))
		Column.InternalSortMode= "ASC"
	Next
	If ShouldRefresh Then
		TABLE_PURCHASE_ORDER.Refresh
		XSelections.Clear
	End If
End Sub
Sub TABLE_PURCHASE_ORDER_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Dim RowData As Map = TABLE_PURCHASE_ORDER.GetRow(RowId)
End Sub
Sub TABLE_PURCHASE_ORDER_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = TABLE_PURCHASE_ORDER.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)
	
	PANEL_BG_SECURITY.SetVisibleAnimated(300,True)
	PANEL_BG_SECURITY.BringToFront
	GET_SECURITY
	security_trigger = "MANUAL TABLE"
	LABEL_LOAD_DESCRIPTION.Text = RowData.Get("Product Description")
	EDITTEXT_PASSWORD.RequestFocus
	LABEL_LOAD_EXPIRATION.Text = "NO EXPIRATION"
	LABEL_LOAD_MANUFACTURED.Text = "NO EXPIRATION"
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_PASSWORD)
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
#Region Scan Barcode
	Dim add_query As String = " AND principal_id = '"&RECEIVING_MODULE.principal_id&"'"
	Dim trigger As Int = 0
	cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE (bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") &"' as INTEGER) or case_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or box_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or bag_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER) or pack_bar_code = CAST ('"& BytesToString(Buffer, 0, Buffer.Length, "UTF8") & "' as INTEGER)) and prod_status = '0'"&add_query&" ORDER BY product_id")
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
				cursor5 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & cursor4.GetString("principal_id") & "'")
				If cursor5.RowCount > 0 Then
					For row1 = 0 To cursor5.RowCount - 1
						cursor5.Position = row1
					Next
					Msgbox2Async("The product you scanned :"& CRLF &""&cursor4.GetString("product_desc")&" "& CRLF &"belongs to principal :"& CRLF &""&cursor5.GetString("principal_name")&""& CRLF &"which your selected principal is :"& CRLF &""&MONTHLY_INVENTORY_MODULE.principal_name&"", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				End If
			Next
		Else
			Msgbox2Async("The barcode you scanned is not REGISTERED IN THE SYSTEM.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
		trigger = 1
	End If
	
	'IF IN PRINCIPAL & EXISTING IN SYSTEM
	If trigger = 0 Then		
		scan_code = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
		cursor7 = connection.ExecQuery("SELECT * FROM purchase_order_ref_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.Text&"'")
		'if in PURCHASE ORDER
		If cursor7.RowCount > 0 Then
			For row = 0 To cursor7.RowCount - 1
				cursor7.Position = row
				GET_DETAILS
				Sleep(0)
				Sleep(100)
				PANEL_BG_INPUT.SetVisibleAnimated(300,True)
				Sleep(100)
				reason = "N/A"
				input_type = "BARCODE"
				EDITTEXT_QUANTITY.Text = ""
				LABEL_LOAD_EXPIRATION.Text = "NO EXPIRATION"
				LABEL_LOAD_MANUFACTURED.Text = "NO EXPIRATION"
				Sleep(100)
				LOAD_DOCUMENT
				Sleep(0)
				LOAD_LIST
			Next
		'NOT IN PURCHASE ORDER
		Else
			If RECEIVING_MODULE.transaction_type = "PURCHASE ORDER" Then
				Msgbox2Async("The product you scanned ("&LABEL_LOAD_DESCRIPTION.Text&") is not IN THE PURCHASE ORDER. Do you want to received this product?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					GET_DETAILS
					Sleep(0)
					Sleep(100)
					PANEL_BG_INPUT.SetVisibleAnimated(300,True)
					Sleep(100)
					reason = "N/A"
					input_type = "BARCODE"
					EDITTEXT_QUANTITY.Text = ""
					LABEL_LOAD_EXPIRATION.Text = "NO EXPIRATION"
					LABEL_LOAD_MANUFACTURED.Text = "NO EXPIRATION"
					Sleep(100)
					LOAD_DOCUMENT
					Sleep(0)
					LOAD_LIST
				End If
			Else If RECEIVING_MODULE.transaction_type = "AUTO SHIP" Then
				GET_DETAILS
				Sleep(0)
				Sleep(100)
				PANEL_BG_INPUT.SetVisibleAnimated(300,True)
				Sleep(100)
				reason = "N/A"
				input_type = "BARCODE"
				EDITTEXT_QUANTITY.Text = ""
				LABEL_LOAD_EXPIRATION.Text = "NO EXPIRATION"
				LABEL_LOAD_MANUFACTURED.Text = "NO EXPIRATION"
				Sleep(100)
				LOAD_DOCUMENT
				Sleep(0)
				LOAD_LIST
			End If
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

#Region DOCUMENT
Sub BUTTON_DOCUMENT_Click
	VIEW_DR
End Sub
Sub CHECK_DOC
	LISTVIEW_DR.Clear
	cursor1 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"'")
	If cursor1.RowCount > 0 Then
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			LISTVIEW_DR.AddTwoLines2(cursor1.GetString("dr_no"),cursor1.GetString("trucking")&"-"&cursor1.GetString("truck_type")&"-"&cursor1.GetString("plate_no")&"-"&cursor1.GetString("driver")&"",cursor1.GetString("dr_no"))
		Next
		BUTTON_OKAY.Visible = True
	Else
		BUTTON_OKAY.Visible = False	
	End If
End Sub
Sub VIEW_DR
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LISTVIEW_DR.Background = bg
	Dim background As ColorDrawable
	PANEL_BG_DELIVERY.SetVisibleAnimated(300, True)
	PANEL_BG_DELIVERY.BringToFront
	background.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_TRUCKING .Background = background
	EDITTEXT_DRIVER.Background = background
	EDITTEXT_DOC_NO.Background = background
	EDITTEXT_TRUCKTYPE.Background = background
	Dim background2 As ColorDrawable
	background2.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_PLATE.Background = background2
	Sleep(0)
	LISTVIEW_DR.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LISTVIEW_DR.TwoLinesLayout.Label.TextSize = 20
	LISTVIEW_DR.TwoLinesLayout.label.Height = 8%y
	LISTVIEW_DR.TwoLinesLayout.Label.TextColor = Colors.Black
	LISTVIEW_DR.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LISTVIEW_DR.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LISTVIEW_DR.TwoLinesLayout.SecondLabel.Top = 7%y
	LISTVIEW_DR.TwoLinesLayout.SecondLabel.TextSize = 10
	LISTVIEW_DR.TwoLinesLayout.SecondLabel.Height = 4%y
	LISTVIEW_DR.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LISTVIEW_DR.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LISTVIEW_DR.TwoLinesLayout.ItemHeight = 12%y
	EDITTEXT_PLATE.RequestFocus
	Sleep(0)
	CHECK_DOC
End Sub
Sub CHECK_PLATE
	cursor2 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE plate_no = '"&EDITTEXT_PLATE.Text.ToUpperCase&"'")
	If cursor2.RowCount > 0 Then
		For i = 0 To cursor2.RowCount - 1
			Sleep(0)
			cursor2.Position = i
			EDITTEXT_DRIVER.Text = cursor2.GetString("driver")
			EDITTEXT_TRUCKING.Text = cursor2.GetString("trucking")
			EDITTEXT_TRUCKTYPE.Text = cursor2.GetString("truck_type")
		Next
		EDITTEXT_DOC_NO.RequestFocus
		Sleep(0)
		CTRL.ShowKeyboard(EDITTEXT_DOC_NO)
	Else
		ToastMessageShow("No history for this plate number.", False)
	End If
End Sub
Sub BUTTON_SEARCH_Click
	CHECK_PLATE
End Sub
Sub BUTTON_SAVE_Click
	
	If EDITTEXT_PLATE.Text = "" Or EDITTEXT_PLATE.Text.Length <= 5 Then
		ToastMessageShow("Please input a valid plate number (5+ char)", False)
	Else If EDITTEXT_TRUCKING.Text = "" Or EDITTEXT_PLATE.Text.Length <= 5 Then
		ToastMessageShow("Please input a valid trucking (5+ char)", False)
	Else If EDITTEXT_TRUCKTYPE.Text = "" Or EDITTEXT_PLATE.Text.Length <= 5 Then
		ToastMessageShow("Please input a valid  valid truck type", False)
	Else If EDITTEXT_DRIVER.Text = "" Or EDITTEXT_PLATE.Text.Length <= 5 Then
		ToastMessageShow("Please input a valid  driver (5+ char)", False)
	Else If EDITTEXT_DOC_NO.Text = "" Or EDITTEXT_PLATE.Text.Length <= 5 Then
		ToastMessageShow("Please input a valid  document number (5+ char)", False)
	Else
		If BUTTON_SAVE.Text = " Save" Then
			cursor3 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' and dr_no = '"&EDITTEXT_DOC_NO.Text.ToUpperCase&"'")
			If cursor3.RowCount > 0 Then
				ToastMessageShow("Delivery No. already exist", False)
			Else
				connection.ExecNonQuery("INSERT INTO receiving_delivery_table VALUES ('"&RECEIVING_MODULE.purchase_order_no&"','"&EDITTEXT_DOC_NO.Text.ToUpperCase&"','"&EDITTEXT_TRUCKING.Text.ToUpperCase& _
			"','"&EDITTEXT_TRUCKTYPE.Text.ToUpperCase&"','"&EDITTEXT_PLATE.Text.ToUpperCase&"','"&EDITTEXT_DRIVER.Text.ToUpperCase&"','"& DateTime.Date(DateTime.Now) &"','"& DateTime.Time(DateTime.Now)& _
			"','"&LOGIN_MODULE.tab_id&"','"&LOGIN_MODULE.username&"')")
				ToastMessageShow("DR ADDED", False)
				EDITTEXT_DOC_NO.Text = ""
				CTRL.HideKeyboard
				Sleep(0)
				CHECK_DOC
			End If
		Else
			cursor3 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' and dr_no = '"&EDITTEXT_DOC_NO.Text.ToUpperCase&"'")
			If cursor3.RowCount > 0 Then
				ToastMessageShow("Delivery No. already exist", False)
			Else
				ProgressDialogShow2("Updating...", False)
				Dim query As String = "UPDATE receiving_delivery_table SET dr_no = ? , trucking = ?, truck_type = ?, driver = ?, user_info = ? WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' and dr_no = '"&old_doc_no&"'"
				connection.ExecNonQuery2(query,Array As String(EDITTEXT_DOC_NO.Text.ToUpperCase,EDITTEXT_TRUCKING.Text.ToUpperCase,EDITTEXT_TRUCKTYPE.Text.ToUpperCase,EDITTEXT_DRIVER.Text.ToUpperCase,LOGIN_MODULE.username))
				Sleep(750)
				Dim query2 As String = "UPDATE receiving_disc_table SET dr_no = ?  WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' and dr_no = '"&old_doc_no&"'"
				connection.ExecNonQuery2(query2,Array As String(EDITTEXT_DOC_NO.Text.ToUpperCase))
				Sleep(750)
				ToastMessageShow("Document Updated", False)
				ProgressDialogHide
				BUTTON_OKAY.Text = " Okay"
				BUTTON_OKAY.TextColor = Colors.Blue
				BUTTON_SAVE.Text = " Save"
				BUTTON_SAVE.TextColor = Colors.RGB(0,255,0)
			End If
		End If
	End If
	Sleep(0)
End Sub
Sub BUTTON_OKAY_Click
	If BUTTON_OKAY.Text = " Cancel" Then
		BUTTON_OKAY.Text = " Okay"
		BUTTON_OKAY.TextColor = Colors.Blue
		BUTTON_SAVE.Text = " Save"
		BUTTON_SAVE.TextColor = Colors.RGB(0,255,0)
		EDITTEXT_DOC_NO.Text = ""
		EDITTEXT_DRIVER.Text= ""
		EDITTEXT_PLATE.Text= ""
		EDITTEXT_TRUCKTYPE.Text= ""
		EDITTEXT_TRUCKING.Text= ""
	Else
		PANEL_BG_DELIVERY.SetVisibleAnimated(300, False)
	End If

End Sub
Sub LISTVIEW_DR_ItemLongClick (Position As Int, Value As Object)
	Msgbox2Async("What do you want to do in this Document No?", "Options", "Edit", "", "Delete", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		old_doc_no = Value
		BUTTON_SAVE.Text = " Edit"
		BUTTON_SAVE.TextColor = Colors.Blue
		BUTTON_OKAY.Text = " Cancel"
		BUTTON_OKAY.TextColor = Colors.Red
		cursor4 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE dr_no = '"&Value&"'")
		If cursor4.RowCount > 0 Then
			For i = 0 To cursor4.RowCount - 1
				Sleep(0)
				cursor4.Position = i
				EDITTEXT_PLATE.Text = cursor4.GetString("plate_no")
				EDITTEXT_DOC_NO.Text = cursor4.GetString("dr_no")
				EDITTEXT_DRIVER.Text = cursor4.GetString("driver")
				EDITTEXT_TRUCKING.Text = cursor4.GetString("trucking")
				EDITTEXT_TRUCKTYPE.Text = cursor4.GetString("truck_type")
			Next
		Else
			ToastMessageShow("No dr data", False)
		End If
	End If
End Sub
#End Region

#Region INPUT
Sub GET_DETAILS
	Dim total_case_order As String
	Dim total_case_received As String
	Dim balance As String
	Dim total_received As Int
	Dim total_pcs_order As Int
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Black)
	EDITTEXT_QUANTITY.Background = bg
	
	cursor8 = connection.ExecQuery("SELECT a.*, b.total_pcs_received as 'total_pcs_received', c.* FROM " & _
	"(Select * FROM product_table WHERE product_desc = '"&LABEL_LOAD_DESCRIPTION.Text&"') As c " & _
	"LEFT JOIN " & _
	"(Select *, sum(total_pieces) As 'total_pcs_order' FROM purchase_order_ref_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.Text&"' GROUP BY po_doc_no, product_description) as a " & _
	"ON a.product_description = c.product_desc " & _
	"LEFT JOIN " & _
	"(Select *, sum(total_pieces) As 'total_pcs_received' FROM receiving_disc_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.Text&"' GROUP BY po_doc_no, product_description) as b " & _
	"ON b.product_description = c.product_desc")
	If cursor8.RowCount > 0 Then
		For ia = 0 To cursor8.RowCount - 1
			Sleep(10)
			cursor8.Position = ia
			If cursor8.GetString("total_pcs_received") = Null Or cursor8.GetString("total_pcs_received") = "" Then
				total_received = 0
				total_case_received = 0
				total_input = 0
				Log(cursor8.GetString("total_pcs_received") & " -")
			Else
				total_received = cursor8.GetString("total_pcs_received")
				total_input = cursor8.GetString("total_pcs_received")
				total_case_received = Number.Format3((total_received / cursor8.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
				Log(cursor8.GetString("total_pcs_received") & " +")
			End If
			
			
			If cursor8.GetString("total_pcs_order") = Null Or cursor8.GetString("total_pcs_order") = "" Then
				total_pcs_order = 0
				total_case_order = 0
				total_order = 0
			Else
				total_pcs_order = cursor8.GetString("total_pcs_order")
				total_order = cursor8.GetString("total_pcs_order")
				total_case_order = Number.Format3((total_pcs_order / cursor8.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
			End If
			
			balance = Number.Format3(((total_received - total_pcs_order) / cursor8.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15)
		
		
			LABEL_LOAD_VARIANT.Text = cursor8.GetString("c.product_variant")
			principal_id = cursor8.GetString("c.principal_id")
			product_id = cursor8.GetString("c.product_id")
			
			If total_case_order.SubString(total_case_order.IndexOf(".")+1) > 0 Then
				LABEL_LOAD_ORDER.Text = Number.Format3((total_pcs_order / cursor8.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15) & " CASE" 
			Else
				LABEL_LOAD_ORDER.Text = Number.Format3((total_pcs_order / cursor8.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15) & " CASE"
			End If
			If total_case_received.SubString(total_case_received.IndexOf(".")+1) > 0 Then
				LABEL_LOAD_INPUT.Text = Number.Format3((total_received / cursor8.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15) & " CASE"
			Else
				LABEL_LOAD_INPUT.Text = Number.Format3((total_received / cursor8.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15) & " CASE"
			End If
			If balance.SubString(balance.IndexOf(".")+1) > 0 Then
				LABEL_LOAD_BALANCE.Text = Number.Format3(((total_received - total_pcs_order) / cursor8.GetString("CASE_UNIT_PER_PCS")),0,2,2,".","","",0,15) & " CASE"
			Else
				LABEL_LOAD_BALANCE.Text = Number.Format3(((total_received - total_pcs_order) / cursor8.GetString("CASE_UNIT_PER_PCS")),0,0,0,".","","",0,15) & " CASE"
			End If
			
			If balance > 0 Then
				LABEL_LOAD_BALANCE.Color = Colors.Blue
			else if balance < 0 Then
				LABEL_LOAD_BALANCE.Color = Colors.Red
			Else
				LABEL_LOAD_BALANCE.Color = Colors.Green
			End If
			
			CMB_UNIT.cmbBox.Clear
			If cursor8.GetString("CASE_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("CASE")
			End If
			If cursor8.GetString("PCS_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("PCS")
			End If
			If cursor8.GetString("DOZ_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("DOZ")
			End If
			If cursor8.GetString("BOX_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("BOX")
			End If
			If cursor8.GetString("BAG_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("BAG")
			End If
			If cursor8.GetString("PACK_UNIT_PER_PCS") > 0 Then
				CMB_UNIT.cmbBox.Add("PACK")
			End If
			Sleep(0)
			If scan_code.Trim = cursor8.GetString("case_bar_code") Then
				CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE")
			End If
			If scan_code.Trim = cursor8.GetString("bar_code") Then
				CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS")
			End If
			If scan_code.Trim = cursor8.GetString("box_bar_code") Then
				CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX")
			End If
			If scan_code.Trim = cursor8.GetString("pack_bar_code") Then
				CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK")
			End If
			If scan_code.Trim = cursor8.GetString("bag_bar_code") Then
				CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG")
			End If

			
			caseper = cursor8.GetString("CASE_UNIT_PER_PCS")
			pcsper = cursor8.GetString("PCS_UNIT_PER_PCS")
			dozper = cursor8.GetString("DOZ_UNIT_PER_PCS")
			boxper = cursor8.GetString("BOX_UNIT_PER_PCS")
			bagper = cursor8.GetString("BAG_UNIT_PER_PCS")
			packper = cursor8.GetString("PACK_UNIT_PER_PCS")
			price = cursor8.GetString("PCS_COMPANY")
			
			default_reading = cursor8.GetString("default_expiration_date_reading")
			lifespan_year = cursor8.GetString("life_span_year")
			lifespan_month = cursor8.GetString("life_span_month")
		Next
		ProgressDialogHide
	Else
		ProgressDialogHide
		ToastMessageShow("Data is empty", False)
	End If
	
End Sub
Sub LOAD_DOCUMENT
	CMB_INVOICE.cmbBox.Clear
	cursor2 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"'")
	If cursor2.RowCount > 0 Then
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			CMB_INVOICE.cmbBox.Add(cursor2.GetString("dr_no"))
		Next
		CMB_INVOICE.SelectedIndex = - 1
		OpenSpinner(CMB_INVOICE.cmbBox)
	End If
End Sub
Sub PANEL_BG_INPUT_Click
	Return True
End Sub
Sub LABEL_LOAD_EXPIRATION_Click
	Dialog.Title = "Select Expiration Date"
	Dialog.TitleBarColor = Colors.RGB(255,109,81)
	Wait For (Dialog.ShowTemplate(DateTemplate, "", "NO EXP", "CANCEL")) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_EXPIRATION.Text = DateTime.Date(DateTemplate.Date)
		OpenSpinner(CMB_UNIT.cmbBox)
		Sleep(10)
		CMB_UNIT.SelectedIndex = -1
		GET_EXPIRATION_SPAN
	Else if Result = xui.DialogResponse_Negative Then
		LABEL_LOAD_EXPIRATION.Text = "NO EXPIRATION"
	End If
End Sub
Sub LABEL_LOAD_MANUFACTURED_Click
	Dialog.Title = "Select Manufacturing Date"
	Dialog.TitleBarColor = Colors.RGB(91,255,81)
	Wait For (Dialog.ShowTemplate(DateTemplate2, "", "NO MFG", "CANCEL")) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_MANUFACTURED.Text = DateTime.Date(DateTemplate2.Date)
		OpenSpinner(CMB_UNIT.cmbBox)
		Sleep(10)
		CMB_UNIT.SelectedIndex = -1
		GET_MANUFACTURING_SPAN
	Else if Result = xui.DialogResponse_Negative Then
		LABEL_LOAD_MANUFACTURED.Text = "NO MANUFACTURING"
	End If
End Sub
Sub BUTTON_EXIT_Click
	PANEL_BG_INPUT.SetVisibleAnimated(300,False)
	LOAD_PURCHASE_ORDER
End Sub
Sub CMB_INVOICE_SelectedIndexChanged (Index As Int)
	If default_reading = "BOTH" Or default_reading = "Expiration Date" Then
		OpenLabel(LABEL_LOAD_EXPIRATION)
	Else If default_reading = "Manufacturing Date" Then
		OpenLabel(LABEL_LOAD_MANUFACTURED)
	Else
		
	End If
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
Sub CMB_UNIT_SelectedIndexChanged (Index As Int)
	EDITTEXT_QUANTITY.RequestFocus
	EDITTEXT_QUANTITY.SelectAll
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
End Sub
Sub DaysBetweenDates(Date1 As Long, Date2 As Long)
	Return Floor((Date2 - Date1) / DateTime.TicksPerDay)
End Sub
Sub BUTTON_ADD_Click
	Dim EXPDATE As String
	Dim DATENOW As String
	
	
	If EDITTEXT_QUANTITY.Text = "" Or EDITTEXT_QUANTITY.Text <= 0 Then
		ToastMessageShow("Please input a valid quantity", False)
	Else
		If LABEL_LOAD_EXPIRATION.Text <> "NO EXPIRATION" Then
			EXPDATE = DateTime.DateParse(LABEL_LOAD_EXPIRATION.Text)
			DATENOW = DateTime.DateParse(DateTime.Date(DateTime.Now))
			If DaysBetweenDates(DATENOW,EXPDATE) <= 0 Then
				ToastMessageShow("You cannot input a expiration date from to date or back date.", False)
			Else If DaysBetweenDates(DATENOW,EXPDATE) <= 120 Then
				Msgbox2Async("The expiration you inputing is 6 months below. Do you want to received this product?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				Wait For Msgbox_Result (Result As Int)
				If Result = DialogResponse.POSITIVE Then
					PANEL_BG_SECURITY.SetVisibleAnimated(300,True)
					PANEL_BG_SECURITY.BringToFront
					GET_SECURITY
					security_trigger = "BELOW SIX"
					EDITTEXT_PASSWORD.RequestFocus
					Sleep(0)
					CTRL.ShowKeyboard(EDITTEXT_PASSWORD)
				End If
			Else
				INPUT_RECEIVED
			End If
		Else
			
		End If
		
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
Sub GET_TRANSACTION_NUMBER
	cursor2 = connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as transaction_number from receiving_disc_table WHERE po_doc_no = '" & RECEIVING_MODULE.purchase_order_no & "'")
	For i = 0 To cursor2.RowCount - 1
		cursor2.Position = i
		If cursor2.GetString("transaction_number") = Null Or cursor2.GetString("transaction_number") = "" Then
			transaction_number =1
		Else
			transaction_number = cursor2.GetString("transaction_number") + 1
		End If
	Next
End Sub
Sub INPUT_RECEIVED
	GET_EXP
	GET_MFG
	Sleep(0)
	If CMB_UNIT.cmbBox.SelectedIndex <= -1 Then CMB_UNIT.cmbBox.SelectedIndex = 0
	If CMB_INVOICE.SelectedIndex <= -1 Then CMB_INVOICE.SelectedIndex = 0
	Sleep(0)
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
	total_price = Number.Format3((total_pieces * price),0,2,2,".","","",0,15)
	
	Dim trigger As Int = 0
	If total_pieces + total_input > total_order Then
		If RECEIVING_MODULE.transaction_type = "PURCHASE ORDER" Then	
			Msgbox2Async("The quantity you will input will over from the quantity of the purchase order, Do you want to continue", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				trigger = 1
			Else
				trigger = 0
			End If
		Else If RECEIVING_MODULE.transaction_type = "AUTO SHIP" Then
			trigger = 1
		End If
	Else
		trigger = 1
	End If
	
	If trigger = 1 Then
		If BUTTON_ADD.Text = " SAVE" Then
			Sleep(0)
			GET_TRANSACTION_NUMBER
			Sleep(0)
			Dim query As String = "INSERT INTO receiving_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			connection.ExecNonQuery2(query,Array As String(RECEIVING_MODULE.purchase_order_no,CMB_INVOICE.cmbBox.SelectedItem,transaction_number, _
			product_id,LABEL_LOAD_VARIANT.Text, LABEL_LOAD_DESCRIPTION.Text,CMB_UNIT.cmbBox.SelectedItem,EDITTEXT_QUANTITY.Text,total_pieces,total_price, _
			input_type, reason, scan_code, _
			monthexp,yearexp,LABEL_LOAD_EXPIRATION.Text,monthmfg,yearmfg,LABEL_LOAD_MANUFACTURED.Text, _
			DateTime.Date(DateTime.now),DateTime.Time(DateTime.Now),LOGIN_MODULE.username,LOGIN_MODULE.tab_id,"EXISTING"))
			Sleep(0)
			ToastMessageShow("Product Expiration Added Succesfully",False)
			CLEAR_INPUT
			Sleep(0)
			GET_DETAILS
			Sleep(0)
			LOAD_LIST
	Else
		Msgbox2Async("Are you sure you want to update this item?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
'			Dim insert_query As String = "INSERT INTO daily_inventory_disc_table_trail SELECT *,'EDITED' as 'edit_type',? as 'edit_by',? as 'date',? as 'time' from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?"
'			connection.ExecNonQuery2(insert_query, Array As String(LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,item_number))
			Sleep(100)
			Dim query As String = "UPDATE receiving_disc_table SET dr_no = ?, unit = ?, quantity = ?, total_pieces = ?, amount= ?, month_expired = ?, year_expired = ?, date_expired = ?, month_manufactured = ?, year_manufactured = ?, date_manufactured = ?, user_info = ? WHERE po_doc_no = ? AND transaction_number = ?"
			connection.ExecNonQuery2(query,Array As String(CMB_INVOICE.cmbBox.SelectedItem,CMB_UNIT.cmbBox.SelectedItem, EDITTEXT_QUANTITY.Text, total_pieces, total_price,monthexp,yearexp,LABEL_LOAD_EXPIRATION.Text,monthmfg,yearmfg,LABEL_LOAD_MANUFACTURED.Text,LOGIN_MODULE.username, RECEIVING_MODULE.purchase_order_no,transaction_number))
			Sleep(0)
			ToastMessageShow("Transaction Updated", False)
			CLEAR_INPUT
			Sleep(0)
			GET_DETAILS
			Sleep(0)
			LOAD_LIST
		End If
	End If
	End If
End Sub
Sub CLEAR_INPUT
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(0,255,70), 5, 0, Colors.LightGray)
	BUTTON_ADD.Background = bg
	BUTTON_ADD.Text = " SAVE"
	EDITTEXT_QUANTITY.Text = ""
	BUTTON_CANCEL.Visible = False
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
	LVL_LIST.TwoLinesLayout.label.Height = 8%y
	LVL_LIST.TwoLinesLayout.Label.TextColor = Colors.Black
	LVL_LIST.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LVL_LIST.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LVL_LIST.TwoLinesLayout.SecondLabel.Top = 7%y
	LVL_LIST.TwoLinesLayout.SecondLabel.TextSize = 12
	LVL_LIST.TwoLinesLayout.SecondLabel.Height = 4%y
	LVL_LIST.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LVL_LIST.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LVL_LIST.TwoLinesLayout.ItemHeight = 12%y
	Dim numbercount As Int = 0

	cursor9 = connection.ExecQuery("SELECT * FROM receiving_disc_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' and product_description = '"&LABEL_LOAD_DESCRIPTION.Text&"' ORDER by transaction_number")
	If cursor9.RowCount > 0 Then
		For i = 0 To cursor9.RowCount - 1
			Sleep(0)
			cursor9.Position = i
			numbercount = numbercount + 1
			LVL_LIST.AddTwoLines2(numbercount & ".) "& cursor9.GetString("quantity") & " " & cursor9.GetString("unit")," DOC NO: " & cursor9.GetString("dr_no") & " EXP DATE: " & cursor9.GetString("date_expired"),cursor9.GetString("transaction_number"))
		Next
	Else

	End If
End Sub
Sub LVL_LIST_ItemClick (Position As Int, Value As Object)
	Msgbox2Async("Please choose a command..", "Option", "EDIT", "CANCEL", "DELETE", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		Dim bg As ColorDrawable
		bg.Initialize2(Colors.RGB(0,167,255), 5, 0, Colors.LightGray)
		BUTTON_ADD.Background = bg
		BUTTON_ADD.Text = " Edit"
		BUTTON_CANCEL.Visible = True
		transaction_number = Value
		cursor6 = connection.ExecQuery("SELECT * FROM receiving_disc_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' AND transaction_number = '"&transaction_number&"'")
		If cursor6.RowCount > 0 Then
			For i = 0 To cursor6.RowCount - 1
				Sleep(0)
				cursor6.Position = i
				CMB_INVOICE.SelectedIndex = CMB_INVOICE.cmbBox.IndexOf(cursor6.GetString("dr_no"))
				Sleep(0)
				LABEL_LOAD_MANUFACTURED.Text = cursor6.GetString("date_manufactured")
				LABEL_LOAD_EXPIRATION.Text = cursor6.GetString("date_expired")
				Sleep(10)
				CMB_UNIT.SelectedIndex = CMB_UNIT.cmbBox.IndexOf(cursor6.GetString("unit"))
				EDITTEXT_QUANTITY.Text = cursor6.GetString("quantity")
				Sleep(0)
				EDITTEXT_QUANTITY.SelectAll
				Sleep(0)
				EDITTEXT_QUANTITY.RequestFocus
				Sleep(0)
				CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
				total_input = total_input - cursor6.GetString("total_pieces")
			Next
		Else

		End If
	Else if Result = DialogResponse.NEGATIVE Then
		Msgbox2Async("Are you sure you want to delete this item?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
'		Dim insert_query As String = "INSERT INTO daily_inventory_disc_table_trail SELECT *,'DELETED' as 'edit_type',? as 'edit_by',? as 'date',? as 'time' from daily_inventory_disc_table WHERE group_id = ? AND inventory_date = ? AND item_number = ?"
'		connection.ExecNonQuery2(insert_query, Array As String(LOGIN_MODULE.username, DateTime.Date(DateTime.Now), DateTime.Time(DateTime.Now),DAILY_INVENTORY_MODULE.group_id,LABEL_LOAD_INV_DATE.Text,Value))
'		End If
			Dim query As String = "DELETE FROM receiving_disc_table WHERE po_doc_no = ? AND transaction_number = ?"
			connection.ExecNonQuery2(query,Array As String(RECEIVING_MODULE.purchase_order_no,Value))
			ToastMessageShow("Deleted Successfully", True)
			GET_DETAILS
'		GET_DAILY
'		Sleep(0)
			LOAD_LIST
		End If
	End If
End Sub
Sub BUTTON_CANCEL_Click
	CLEAR_INPUT
End Sub
#End Region

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
			If security_trigger = "MANUAL TABLE" Then
				Dim ls As List
				ls.Initialize
				ls.Add("BARCODE NOT REGISTERED IN THE SYSTEM")
				ls.Add("NO ACTUAL BARCODE")
				ls.Add("NO SCANNER")
				ls.Add("SCANNER CAN READ BARCODE")
				ls.Add("DAMAGE BARCODE")
				InputListAsync(ls, "Choose reason", -1, True) 'show list with paired devices
				Wait For InputList_Result (Result2 As Int)
				If Result2 <> DialogResponse.CANCEL Then
					PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
					EDITTEXT_PASSWORD.Text = ""
					CTRL.HideKeyboard
					Sleep(0)
					reason = ls.Get(Result2)
					GET_DETAILS
					Sleep(100)
					PANEL_BG_INPUT.SetVisibleAnimated(300,True)
					Sleep(100)
					input_type = "MANUAL " & CMB_ACCOUNT.cmbBox.SelectedItem
					scan_code = "N/A"
					EDITTEXT_QUANTITY.Text = ""
					Sleep(100)
					LOAD_DOCUMENT
					Sleep(0)
					LOAD_LIST
				End If
			Else If security_trigger = "MANUAL BUTTON" Then
				PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
				EDITTEXT_PASSWORD.Text = ""
				CTRL.HideKeyboard
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
					Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate, "", "", "CANCEL")
'				Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
					Wait For (rs) Complete (Result As Int)
					If Result = xui.DialogResponse_Positive Then
						LABEL_LOAD_DESCRIPTION.Text = SearchTemplate.SelectedItem
						Sleep(0)
						reason = ls.Get(Result2)
						Sleep(0)
						GET_MANUAL_ORDER
					End If
				End If
			Else if security_trigger = "BELOW SIX" Then
				PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
				EDITTEXT_PASSWORD.Text = ""
				CTRL.HideKeyboard
				Sleep(0)
				INPUT_RECEIVED
			End If
		Else
			ToastMessageShow("Wrong Password",True)
		End If
	End If
End Sub
Sub BUTTON_SECURITY_CANCEL_Click
	PANEL_BG_SECURITY.SetVisibleAnimated(300, False)
	EDITTEXT_PASSWORD.Text = ""
	CTRL.HideKeyboard
End Sub

Sub INPUT_MANUAL
	Dialog.Title = "Find Product"
	Dim principal_query As String  = "AND principal_id = '"&RECEIVING_MODULE.principal_id&"'"
	Sleep(0)
	SearchTemplate.CustomListView1.Clear
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' "&principal_query&" ORDER BY product_desc ASC")
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		Items.Add(cursor2.GetString("product_desc"))
	Next
	SearchTemplate.SetItems(Items)
End Sub
Sub GET_MANUAL_ORDER
	cursor7 = connection.ExecQuery("SELECT * FROM purchase_order_ref_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"' AND product_description = '"&LABEL_LOAD_DESCRIPTION.Text&"'")
	'if in PURCHASE ORDER
	If cursor7.RowCount > 0 Then
		For row = 0 To cursor7.RowCount - 1
			cursor7.Position = row
			GET_DETAILS
			Sleep(100)
			PANEL_BG_INPUT.SetVisibleAnimated(300,True)
			Sleep(100)
			input_type = "MANUAL " & CMB_ACCOUNT.cmbBox.SelectedItem
			scan_code = "N/A"
			EDITTEXT_QUANTITY.Text = ""
			Sleep(100)
			LOAD_DOCUMENT
			Sleep(0)
			LOAD_LIST
		Next
		'NOT IN PURCHASE ORDER
	Else
		If RECEIVING_MODULE.transaction_type = "PURCHASE ORDER" Then	
			Msgbox2Async("The produt you scanned ("&LABEL_LOAD_DESCRIPTION.Text&") is not IN THE PURCHASE ORDER. Do you want to received this product?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				GET_DETAILS
				Sleep(100)
				PANEL_BG_INPUT.SetVisibleAnimated(300,True)
				Sleep(100)
				input_type = "MANUAL " & CMB_ACCOUNT.cmbBox.SelectedItem
				scan_code = "N/A"
				EDITTEXT_QUANTITY.Text = ""
				Sleep(100)
				LOAD_DOCUMENT
				Sleep(0)
				LOAD_LIST
			End If
		Else If RECEIVING_MODULE.transaction_type = "AUTO SHIP" Then
			GET_DETAILS
			Sleep(100)
			PANEL_BG_INPUT.SetVisibleAnimated(300,True)
			Sleep(100)
			input_type = "MANUAL " & CMB_ACCOUNT.cmbBox.SelectedItem
			scan_code = "N/A"
			EDITTEXT_QUANTITY.Text = ""
			Sleep(100)
			LOAD_DOCUMENT
			Sleep(0)
			LOAD_LIST
		End If
	End If
End Sub

Sub BUTTON_MANUAL_Click
	PANEL_BG_SECURITY.SetVisibleAnimated(300,True)
	PANEL_BG_SECURITY.BringToFront
	GET_SECURITY
	security_trigger = "MANUAL BUTTON"
	EDITTEXT_PASSWORD.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_PASSWORD)
End Sub

#Region Calculator
Sub oncal
	LoadTexts
	
	Scale_Calc.SetRate(0.6)
	ScaleAuto = Scale_Calc.GetScaleDS

	
'	If 100%x < 100%y Then
'		Scale.HorizontalCenter(pnlKeyboard)
'		Scale.SetBottom(pnlKeyboard, 100%y - 8dip * ScaleAuto)
'		
'		Scale.HorizontalCenter(scvPaperRoll)
'		Scale.SetTopAndBottom2(scvPaperRoll, Activity, 8, pnlKeyboard, 8)
'	Else
'		Scale.VerticalCenter(pnlKeyboard)
'		Scale.SetRight(pnlKeyboard, 100%x)
'		
'		Scale.SetLeftAndRight(scvPaperRoll, 8dip * ScaleAuto, pnlKeyboard.Left - 5dip * ScaleAuto)
'		Scale.SetTopAndBottom(scvPaperRoll, 5dip * ScaleAuto, 100%y - 5dip * ScaleAuto)
'	End If
	
	lblPaperRoll.Initialize("lblPaperRoll")
	scvPaperRoll.Panel.AddView(lblPaperRoll, 0, 0, 100%x, scvPaperRoll.Height)
	scvPaperRoll.Panel.Height = scvPaperRoll.Height
	lblPaperRoll.TextSize = 22 * ScaleAuto
	lblPaperRoll.Color = Colors.White
	lblPaperRoll.TextColor = Colors.Black
	
'	imgCharSizeDown.Initialize(File.DirAssets, "btnCharSizeDown.bmp")
'	imgCharSizeUp.Initialize(File.DirAssets, "btnCharSizeUp.bmp")
	'
'	bd1.Initialize(imgCharSizeUp)
'	bd2.Initialize(imgCharSizeDown)
'	sd.Initialize
'	Dim states(2) As Int
'	states(0) = sd.state_unchecked
'	states(1) = -sd.state_checked
'	sd.addState2(states, bd1)
'	Dim states(1) As Int
'	states(0) = sd.state_checked
'	sd.addState2(states, bd2)
'	btnCharSize.Background = sd
End Sub
Sub btnDigit_Click
	Dim bs As String, Send As View
	
	If New1 = 0 Then
		New1 = 1
	End If
	
	Send = Sender
	bs = Send.Tag
	
	Select bs
		Case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "3.1415926535897932", "."
			Select Op0
				Case "g", "s", "m", "x"
					Return
			End Select

			If Op0 = "e" Then
				Txt = Txt & CRLF & CRLF
				sVal = ""
				Op0 = ""
				Result1 = 0
				New1 = 0
			End If

			If bs = "3.1415926535897932" Then
				If sVal <> "" Then
					Return
				End If
				Txt = Txt & cPI
				sVal = cPI
			Else If bs = "." Then
				If sVal.IndexOf(".") < 0 Then
					Txt = Txt & bs
					sVal = sVal & bs
				End If
			Else
				Txt = Txt & bs
				sVal = sVal & bs
			End If
		Case "a", "b", "c", "d", "e", "y"
			If sVal ="" Then
				Select Op0
					Case "a", "b", "c", "d", "y", ""
						Return
				End Select
				sVal = Result1
			End If
			GetValue(bs)
		Case "g", "s", "x"
			If sVal = "" Then
				Select Op0
					Case "a", "b", "c", "d", "y", ""
						Return
				End Select
				sVal = Result1
			End If
			If Op0 = "" Then
				Result1 = sVal
			End If
			GetValue(bs)
		Case "f"
			If Op0 = "e" Then
				Txt = Txt & CRLF & CRLF
				New1 = 0
				sVal = ""
				Op0 = ""
			End If
	End Select

	UpdateTape
End Sub
Sub GetValue(Op As String)
	If Op0 = "e" And (Op = "s" Or Op = "g" Or Op = "x") Then
		Val = Result1
	Else
		Val = sVal
	End If
	
	sVal = ""
	
	If Op = "g" Or Op = "s" Or Op = "x" Then
		If Op0 = "a" Or Op0 = "b" Or Op0 = "c" Or Op0 = "d" Or Op0 = "y" Then
			CalcResult1(Op0)
			Txt = Txt & "  = " & Result1
		End If
		Operation(Op)
		CalcResult1(Op)
		Txt = Txt & "  = " & Result1
		Op0 = Op
		Op0 = "e"
		Op = "e"
		Return
	End If
	
	If New1 = 1 Then
		Result1 = Val
		Operation(Op)
		If Op = "g" Or Op = "s" Or Op = "x" Then
			CalcResult1(Op)
			Txt = Txt & "  = " & Result1
			Op = "e"
		End If
		UpdateTape
		New1 = 2
		Op0 = Op
		Return
	End If
	
	If Op = "e" Then
		If Op0 = "e" Then
			Return
		End If
		Txt = Txt & CRLF & " =  "
		CalcResult1(Op0)
		Txt = Txt & Result1
	Else
		If Op0 = "g" Or Op0 = "s" Or Op0 = "x" Then
			Operation(Op)
			Op0 = Op
			Return
		End If
		
		CalcResult1(Op0)
		If Op0<>"e" Then
			Txt = Txt & "  = " & Result1
		End If
		Operation(Op)
		If Op = "g" Or Op = "s" Or Op = "x" Then
			CalcResult1(Op)
			Txt = Txt & "  = " & Result1
			Op = "e"
		End If
	End If
	Op0 = Op
End Sub
Sub Operation(Op As String)
	Select Op
		Case "a"
			Txt = Txt & CRLF & "+ "
		Case "b"
			Txt = Txt & CRLF & "- "
		Case "c"
			Txt = Txt & CRLF & "× "
		Case "d"
			Txt = Txt & CRLF & "/ "
		Case "g"
			Txt = Txt & CRLF & "x2 "
		Case "s"
			Txt = Txt & CRLF & "√ "
		Case "x"
			Txt = Txt & CRLF & "1/x "
		Case "y"
			Txt = Txt & CRLF & "% "
	End Select
End Sub
Sub CalcResult1(Op As String)
	Select Op
		Case "a"
			Result1 = Result1 + Val
		Case "b"
			Result1 = Result1 - Val
		Case "c"
			Result1 = Result1 * Val
		Case "d"
			Result1 = Result1 / Val
		Case "g"
			Result1 = Result1 * Result1
		Case "s"
			Result1 = Sqrt(Result1)
		Case "x"
			If Result1 <> 0 Then
				Result1 = 1 / Result1
			End If
		Case "y"
			Result1 = Result1 * Val / 100
	End Select
	Dim res As String = Result1
	LABEL_LOAD_ANSWER.Text = res
End Sub
Sub UpdateTape
	Dim hr As Float
	
	lblPaperRoll.Text = Txt

	hr = stu.MeasureMultilineTextHeight(lblPaperRoll, Txt)
	If hr > scvPaperRoll.Height Then
		lblPaperRoll.Height = hr
		scvPaperRoll.Panel.Height = hr
		Sleep(0)
		scvPaperRoll.ScrollPosition = hr
	End If
End Sub
Sub btnClr_Click
'	Dim Answer As Int

	''	Answer = Msgbox2("Do you really want to clear the calculation?","A T T E N T I O N","Yes","","No",Null)
'	Answer = Msgbox2(Texts(4),Texts(5),Texts(2),"",Texts(3),Null)
	'
'	If Answer = -1 Then
	Val = 0
	sVal = ""
	Result1 = 0
	New1 = 0
	Txt = ""
	Op0 = ""
	lblPaperRoll.Text = ""
	lblPaperRoll.Height = scvPaperRoll.Height
	scvPaperRoll.Panel.Height = scvPaperRoll.Height
	LABEL_LOAD_ANSWER.Text = "0"
	'End If
End Sub
Sub btnBack_Click
	If sVal.Length > 0 Then
		Txt = sVal.SubString2(0, sVal.Length - 1)
		sVal = sVal.SubString2(0, sVal.Length - 1)
		UpdateTape
	End If
End Sub
Sub btnExit_Click
	Val = 0
	sVal = ""
	Result1 = 0
	New1 = 0
	Txt = ""
	Op0 = ""
	lblPaperRoll.Text = ""
	lblPaperRoll.Height = scvPaperRoll.Height
	scvPaperRoll.Panel.Height = scvPaperRoll.Height
	EDITTEXT_QUANTITY.Text = LABEL_LOAD_ANSWER.Text
	PANEL_BG_CALCU.SetVisibleAnimated(300,False)
End Sub
Sub btnCharSize_CheckedChange(Checked As Boolean)
	If Checked = False Then
		lblPaperRoll.TextSize = 16 * ScaleAuto
	Else
		lblPaperRoll.TextSize = 22 * ScaleAuto
	End If
End Sub
Sub LoadTexts
	Dim FileName As String
	Dim iq As Int

'	Locale.Initialize
'	LanguageID = Locale.Language
	FileName = "tapecalc_" & LanguageID & ".txt"
	If File.Exists(ProgPath, FileName) = False Then
		FileName = "tapecalc_en.txt"
	End If
	
	Dim Reader As TextReader
	Reader.Initialize(File.OpenInput(ProgPath, FileName))
	iq = 0
	Texts(iq) = Reader.ReadLine
	Do While Texts(iq) <> Null
		iq = iq + 1
		Texts(iq) = Reader.ReadLine
	Loop
	Reader.Close
End Sub
Sub BUTTON_EXIT_CALCU_Click
	PANEL_BG_CALCU.SetVisibleAnimated(300,False)
End Sub
Sub BUTTON_CALCU_Click
	oncal
	PANEL_BG_CALCU.SetVisibleAnimated(300,True)
	PANEL_BG_CALCU.BringToFront
End Sub
Sub PANEL_BG_CALCU_Click
	Return True
End Sub
#End Region

Sub BUTTON_LIST_Click
	LOAD_INVOICE
	Sleep(0)
	GET_TOTAL_AMT
	Sleep(0)
	LOAD_PER_INVOICE
	Sleep(0)
	GET_TOTAL_CASE
	Sleep(0)
	PANEL_BG_INVOICE.SetVisibleAnimated(300,True)
	PANEL_BG_INVOICE.BringToFront
End Sub
Sub LOAD_INVOICE
	CMB_INV.cmbBox.Clear
	cursor2 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"'")
	If cursor2.RowCount > 0 Then
		For row = 0 To cursor2.RowCount - 1
			cursor2.Position = row
			CMB_INV.cmbBox.Add(cursor2.GetString("dr_no"))
		Next
	End If
End Sub
Sub GET_TOTAL_AMT
	cursor1 = connection.ExecQuery("SELECT sum(amount) as 'amt' FROM receiving_disc_table WHERE dr_no = '"&CMB_INV.cmbBox.SelectedItem&"' AND po_doc_no = '"& RECEIVING_MODULE.purchase_order_no &"'")
	If cursor1.RowCount > 0 Then
		For row = 0 To cursor1.RowCount - 1
			cursor1.Position = row
			If cursor1.GetString("amt") = Null Then
				LABEL_LOAD_TOTALAMT.text = "₱0"
			Else
				LABEL_LOAD_TOTALAMT.text = "₱" & Number.Format3(cursor1.GetString("amt"),0,2,2,".",",",",",0,15)
			End If
		Next
	Else

	End If
End Sub
Sub GET_TOTAL_CASE
	Dim total_case_dr As String
	cursor4 = connection.ExecQuery("SELECT sum(total_case) as 'total_case_dr' FROM " & _
	"(Select a.*, sum(total_pieces) As 'tot_pcs', b.CASE_UNIT_PER_PCS, (sum(total_pieces) / b.CASE_UNIT_PER_PCS) as 'total_case' FROM " & _
	"receiving_disc_table As a " & _
	"LEFT JOIN " & _
	"product_table As b " & _
	"ON a.product_id = b.product_id " & _
	"WHERE a.dr_no = '"&CMB_INV.cmbBox.SelectedItem&"' AND a.po_doc_no = '"& RECEIVING_MODULE.purchase_order_no &"' " & _
	"GROUP BY a.product_id, a.unit) ")
	If cursor4.RowCount > 0 Then
		For row = 0 To cursor4.RowCount - 1
			cursor4.Position = row
			If cursor4.GetString("total_case_dr") = Null Then
				LABEL_LOAD_TOTALCASE.text = "0"
			Else
				total_case_dr = Number.Format3(cursor4.GetString("total_case_dr"),0,2,2,".","","",0,15)
				
				If total_case_dr.SubString(total_case_dr.IndexOf(".")+1) > 0 Then
					LABEL_LOAD_TOTALCASE.text = Number.Format3(cursor4.GetString("total_case_dr"),0,2,2,".","","",0,15)
				Else
					LABEL_LOAD_TOTALCASE.text = Number.Format3(cursor4.GetString("total_case_dr"),0,0,0,".","","",0,15)
				End If
			End If
		Next
	Else

	End If
End Sub
Sub BUTTON_INVOICE_EXIT_Click
	PANEL_BG_INVOICE.SetVisibleAnimated(300,False)
End Sub

Sub LOAD_INVOICE_HEADER
	NameColumn2(0)=TABLE_INVOICE.AddColumn("Product Variant", TABLE_INVOICE.COLUMN_TYPE_TEXT)
	NameColumn2(1)=TABLE_INVOICE.AddColumn("Product Description", TABLE_INVOICE.COLUMN_TYPE_TEXT)
	NameColumn2(2)=TABLE_INVOICE.AddColumn("Unit", TABLE_INVOICE.COLUMN_TYPE_TEXT)
	NameColumn2(3)=TABLE_INVOICE.AddColumn("Qty", TABLE_INVOICE.COLUMN_TYPE_TEXT)
	NameColumn2(4)=TABLE_INVOICE.AddColumn("Amount", TABLE_INVOICE.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_PER_INVOICE
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor3 = connection.ExecQuery("SELECT *, sum(quantity) as 'qty', sum(amount) as 'amt' FROM receiving_disc_table WHERE dr_no = '"&CMB_INV.cmbBox.SelectedItem&"' AND po_doc_no = '"& RECEIVING_MODULE.purchase_order_no &"' GROUP BY product_id, unit ORDER BY product_variant")
	If cursor3.RowCount > 0 Then
		For ia = 0 To cursor3.RowCount - 1
			Sleep(10)
			cursor3.Position = ia
			
			Dim row(5) As Object
			row(0) = cursor3.GetString("product_variant")
			row(1) = cursor3.GetString("product_description")
			row(2) = cursor3.GetString("unit")
			row(3) = cursor3.GetString("qty")
			row(4) = Number.Format3(cursor3.GetString("amt"),0,2,2,".",",",",",0,15)
			Data.Add(row)
		Next
		TABLE_INVOICE.RowHeight = 30dip
		Sleep(100)
		ProgressDialogHide
	Else
		ProgressDialogHide
		ToastMessageShow("Data is empty", False)
	End If
	TABLE_INVOICE.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_INVOICE)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_INVOICE_DataUpdated
	Dim ShouldRefresh As Boolean
	
	For Each Column As B4XTableColumn In Array (NameColumn2(0),NameColumn2(1),NameColumn2(2), NameColumn2(3),NameColumn2(4))
		Dim MaxWidth As Int
		For i = 0 To TABLE_INVOICE.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
'			lbl.SetTextAlignment(Gravity.RIGHT,Gravity.CENTER)
'			MaxHeight = Max(MaxHeight, cvs.MeasureText(lbl.Text, lbl.Font).Height + 10dip)
		Next	
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
	Next

	For Each Column As B4XTableColumn In Array (NameColumn2(0),NameColumn2(1),NameColumn2(2), NameColumn2(3),NameColumn2(4))

		For i = 0 To TABLE_INVOICE.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			lbl.Font = xui.CreateDefaultBoldFont(12)
		Next
	Next
		
	If ShouldRefresh Then
		TABLE_INVOICE.Refresh
		XSelections.Clear
	End If
End Sub

Sub CMB_INV_SelectedIndexChanged (Index As Int)
	GET_TOTAL_AMT
	Sleep(0)
	LOAD_PER_INVOICE
	Sleep(0)
	GET_TOTAL_CASE
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
Sub DELETE_RECEIVING_DISC
	PANEL_BG_MSGBOX.SetVisibleAnimated(300,True)
	PANEL_BG_MSGBOX.BringToFront
	LABEL_HEADER_TEXT.Text = "Uploading Data"
	LABEL_MSGBOX2.Text = "Fetching Data..."
	LABEL_MSGBOX1.Text = "Loading, Please wait..."
	Dim cmd As DBCommand = CreateCommand("delete_receiving_disc", Array(RECEIVING_MODULE.purchase_order_no))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_DAILY_DISC
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
			DELETE_RECEIVING_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_DAILY_DISC
	error_trigger = 0
	cursor7 = connection.ExecQuery("SELECT * FROM receiving_disc_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"'")
	If cursor7.RowCount > 0 Then
		For i = 0 To cursor7.RowCount - 1
			cursor7.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_receiving_disc", Array As String(cursor7.GetString("po_doc_no"),cursor7.GetString("dr_no"),cursor7.GetString("transaction_number"),cursor7.GetString("product_id"),cursor7.GetString("product_variant"), _
			cursor7.GetString("product_description"),cursor7.GetString("unit"),cursor7.GetString("quantity"),cursor7.GetString("total_pieces"),cursor7.GetString("amount"),cursor7.GetString("transaction_type"), _
			cursor7.GetString("reason"),cursor7.GetString("scan_code"),cursor7.GetString("month_expired"),cursor7.GetString("year_expired"),cursor7.GetString("date_expired"),cursor7.GetString("month_manufactured"), _
			cursor7.GetString("year_manufactured"),cursor7.GetString("date_manufactured"),cursor7.GetString("date_registered"),cursor7.GetString("time_registered"),cursor7.GetString("user_info"),cursor7.GetString("tab_id"), _
			cursor7.GetString("status")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor7.GetString("product_description")
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
		DELETE_RECEIVING_DELIVERY
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Updating Failed", False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RECEIVING_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub DELETE_RECEIVING_DELIVERY
	Dim cmd As DBCommand = CreateCommand("delete_receiving_delivery", Array(RECEIVING_MODULE.purchase_order_no))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		Sleep(100)
		INSERT_DAILY_DELIVERY
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
			DELETE_RECEIVING_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
Sub INSERT_DAILY_DELIVERY
	error_trigger = 0
	cursor8 = connection.ExecQuery("SELECT * FROM receiving_delivery_table WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"'")
	If cursor8.RowCount > 0 Then
		For i = 0 To cursor8.RowCount - 1
			cursor8.Position = i
			Sleep(100)
			Dim cmd As DBCommand = CreateCommand("insert_receiving_delivery", Array As String(cursor8.GetString("po_doc_no"),cursor8.GetString("dr_no"),cursor8.GetString("trucking"),cursor8.GetString("truck_type"),cursor8.GetString("plate_no"), _
			cursor8.GetString("driver"),cursor8.GetString("date_registered"),cursor8.GetString("time_registered"),cursor8.GetString("tab_id"),cursor8.GetString("user_info")))
			Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
			LABEL_MSGBOX2.Text = "Uploading : " & cursor8.GetString("trucking")
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
		UPDATE_RECEIVING
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Updating Failed", False)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RECEIVING_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
End Sub
Sub UPDATE_RECEIVING
	LABEL_MSGBOX2.Text = "Updating Status..."
	Dim cmd As DBCommand = CreateCommand("update_receiving_ref", Array(DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),"RECEIVED", RECEIVING_MODULE.purchase_order_no))
	Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(js) JobDone(js As HttpJob)
	If js.Success Then
		connection.ExecNonQuery("UPDATE receiving_ref_table SET received_date = '"&DateTime.Date(DateTime.Now)&"', received_time = '"&DateTime.Time(DateTime.Now)& _
		"', po_status = 'RECEIVED' WHERE po_doc_no = '"&RECEIVING_MODULE.purchase_order_no&"'")
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		ToastMessageShow("Transaction Uploaded Succefully", False)
		Activity.Finish
		StartActivity(RECEIVING_MODULE)
	Else
		error_trigger = 1
		PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
		Log("ERROR: " & js.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
		Sleep(1000)
		Msgbox2Async("Uploading failed, do you want to try uploading again?", "WARNING!", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			DELETE_RECEIVING_DISC
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300,False)
			ToastMessageShow("Updating Failed", False)
		End If
	End If
	js.Release
End Sub
#End Region

Sub PANEL_BG_DELIVERY_Click
	Return True
End Sub

Sub PANEL_BG_MSGBOX_Click
	Return True
End Sub

Sub PANEL_BG_INVOICE_Click
	Return True
End Sub

Sub PANEL_BG_SECURITY_Click
	Return True
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		Return True
	End If
End Sub