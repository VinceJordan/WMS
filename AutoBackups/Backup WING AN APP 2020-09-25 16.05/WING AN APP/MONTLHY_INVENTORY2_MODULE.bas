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
	
	'tss
	Dim TTS1 As TTS
	
	'product
	Public principal_id As String
	Public product_id As String
	Public assign1 As String
	Public assign2 As String
	Public inventory_type As String
	Dim reason As String
	Dim input_type As String
	Dim caseper As String
	Dim pcsper As String
	Dim dozper As String
	Dim boxper As String
	Dim bagper As String
	Dim packper As String
	Dim total_pieces As String
	Dim scan_code As String
	
	'bluetooth
	Dim serial1 As Serial
	Dim AStream As AsyncStreams
	Dim Ts As Timer
	
	Dim cmb_trigger As Int
	
	Dim tableBitmap As Bitmap
	Dim cartBitmap As Bitmap
	Dim calcBitmap As Bitmap
	
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
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	'ime
	Dim CTRL As IME
	
	'bluetooth
	Dim ScannerMacAddress As String
	Dim ScannerOnceConnected As Boolean
	
	'	calcu
	Dim btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9 As Button
	Dim btnBack, btnClr, btnExit As Button
	Dim lblPaperRoll As Label
	Dim scvPaperRoll As ScrollView
	Dim pnlKeyboard As Panel
	Dim stu As StringUtils
	
	Private cvs As B4XCanvas
	Private xui As XUI
	
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private SearchTemplate As B4XSearchTemplate
	Private SearchTemplate2 As B4XSearchTemplate
	Private SearchTemplate3 As B4XSearchTemplate
	
	Private SV_MONTHLY As ScrollView
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
	Private CMB_INVTYPE As B4XComboBox
	Private LABEL_LOAD_PERSON1 As Label
	Private LABEL_LOAD_PERSON2 As Label
	
	Private EDITTEXT_QUANTITY As EditText
	Private CMB_UNIT As B4XComboBox
	Private LABEL_LOAD_DESCRIPTION As Label
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_PRINCIPAL As Label
	Private LABEL_LOAD_BARCODE As Label
	Private CMB_POSITION As B4XComboBox
	Private PANEL_INPUT As Panel
	Private SWITCH_CONTINUES As B4XSwitch
	Private LABEL_CONTINUES As Label
	Private LABEL_LOAD_ANSWER As Label
	Private PANEL_BG_CALCU As Panel
	Private BUTTON_ADD As Button
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
	Activity.LoadLayout("monthly2")
	
	tableBitmap = LoadBitmap(File.DirAssets, "table.png")
	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	calcBitmap = LoadBitmap(File.DirAssets, "calcu.png")
	
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
	SV_MONTHLY.Panel.LoadLayout("monthly2_scrollview")
	SV_MONTHLY.Panel.Height = PANEL_INPUT.Top + PANEL_INPUT.Height + 8dip

	'blueetooth
	serial1.Initialize("Serial")
	Ts.Initialize("Timer", 2000)

'	Dim p As B4XView = xui.CreatePanel("")
'	p.SetLayoutAnimated(0, 10%x, 20%y, 80%y, 40%y)
'	cvs.Initialize(p)
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
	
	SearchTemplate3.Initialize
	SearchTemplate3.CustomListView1.DefaultTextBackgroundColor = Colors.White
	SearchTemplate3.CustomListView1.DefaultTextColor = Colors.Black
	SearchTemplate3.SearchField.TextField.TextColor = Colors.Black
	SearchTemplate3.ItemHightlightColor = Colors.White
	SearchTemplate3.TextHighlightColor = Colors.RGB(82,169,255)
#End Region

	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Transparent)
	EDITTEXT_QUANTITY.Background = bg
	
	CONTINUES
	
	POPULATE_INVTYPE
	Sleep(0) 
	POPULATE_INVPOSITION
	
	PRINCIPAL_SELECT
	
	COUNT_TRANSACTION
	Sleep(0)
	INPUT_MANUAL
	
	INV_COLOR
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "calcu", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("calcu", calcBitmap)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(2, 2, "table", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	Sleep(0)
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "cart", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
	Sleep(0)
End Sub

Sub Activity_Resume
	Log("Resuming...")
	If TTS1.IsInitialized = False Then
		TTS1.Initialize("TTS1")
	End If
	
	Sleep(0)
	If cmb_trigger = 1 Then
		OpenSpinner(CMB_INVTYPE.cmbBox)
		ShowPairedDevices
		If ScannerOnceConnected=True Then
			Ts.Enabled=True
		End If
	Else
		COUNT_TRANSACTION
	End If
End Sub
Sub Activity_Pause (UserClosed As Boolean)
	Log ("Activity paused. Disconnecting...")
'	AStream.Close
'	serial1.Disconnect
'	Ts.Enabled=False
End Sub

Sub OpenSpinner(se As Spinner)
	Dim reflect As Reflector
	reflect.Target = se
	reflect.RunMethod("performClick")
End Sub
Sub OpenLabel(bt As Label)
	Dim reflect As Reflector
	reflect.Target = bt
	reflect.RunMethod("performClick")
End Sub

Sub MainForm_Resize (Width As Double, Height As Double)
	If Dialog.Visible Then Dialog.Resize(Width, Height)
End Sub

#Region INVENTORY DETAILS
Sub POPULATE_INVTYPE
	CMB_INVTYPE.cmbBox.Clear
	CMB_INVTYPE.cmbBox.DropdownTextColor = Colors.Black
	CMB_INVTYPE.cmbBox.TextColor = Colors.Black
	CMB_INVTYPE.cmbBox.Add("COUNT")
	CMB_INVTYPE.cmbBox.Add("RECOUNT")
End Sub
Sub CMB_INVTYPE_SelectedIndexChanged (Index As Int)
	INV_COLOR
	Sleep(0)
	OpenLabel(LABEL_LOAD_PERSON1)
End Sub
Sub INV_COLOR
	If CMB_INVTYPE.SelectedIndex = 0 Then
		CMB_INVTYPE.mBase.Color = xui.Color_Red
	Else
		CMB_INVTYPE.mBase.Color = xui.Color_green
	End If
End Sub
Sub POPULATE_INVPOSITION
	CMB_POSITION.cmbBox.Clear
	CMB_POSITION.cmbBox.DropdownTextColor = Colors.Black
	CMB_POSITION.cmbBox.TextColor = Colors.Black
	CMB_POSITION.cmbBox.Add("Isle 1")
	CMB_POSITION.cmbBox.Add("Isle 2")
	CMB_POSITION.cmbBox.Add("Isle 3")
	CMB_POSITION.cmbBox.Add("Isle 4")
	CMB_POSITION.cmbBox.Add("Isle 5")
	CMB_POSITION.cmbBox.Add("Isle 6")
	CMB_POSITION.cmbBox.Add("Isle 7")
	CMB_POSITION.cmbBox.Add("Isle 8")
	CMB_POSITION.cmbBox.Add("Room 1")
	CMB_POSITION.cmbBox.Add("Room 2")
	CMB_POSITION.cmbBox.Add("Room 3")
	CMB_POSITION.cmbBox.Add("Room 4")
	CMB_POSITION.cmbBox.Add("Room 5")
	CMB_POSITION.cmbBox.Add("No specific position")
End Sub
Sub LABEL_LOAD_PERSON1_Click
	ProgressDialogShow2("Loading...", False)
	INPUT_PERSON1
	Sleep(1000)
	ProgressDialogHide
	Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate, "", "ENTER", "CANCEL")
	Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
	Wait For (rs) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_PERSON1.Text = SearchTemplate.SelectedItem
		Msgbox2Async("Do you want to assign second person?", "Second Person", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		Wait For Msgbox_Result (Result As Int)
		If Result = DialogResponse.POSITIVE Then
			OpenLabel(LABEL_LOAD_PERSON2)
		Else
			OpenSpinner(CMB_POSITION.cmbBox)
		End If
	Else If Result = xui.DialogResponse_Negative Then
		If SearchTemplate.SearchField.Text = "" Then
			LABEL_LOAD_PERSON1.Text = "Assign Person"
		Else
			LABEL_LOAD_PERSON1.Text = SearchTemplate.SearchField.Text.ToUpperCase
			Msgbox2Async("Do you want to assign second person?", "Second Person", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				OpenLabel(LABEL_LOAD_PERSON2)
			Else
				OpenSpinner(CMB_POSITION.cmbBox)
			End If
		End If
		OpenSpinner(CMB_POSITION.cmbBox)
	Else
		LABEL_LOAD_PERSON1.Text = "Assign Person"
		OpenSpinner(CMB_POSITION.cmbBox)
	End If
End Sub
Sub LABEL_LOAD_PERSON2_Click
	ProgressDialogShow2("Loading...", False)
	INPUT_PERSON2
	Sleep(1000)
	ProgressDialogHide
	Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate3, "", "ENTER", "CANCEL")
	Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
	Wait For (rs) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_PERSON2.Text = SearchTemplate3.SelectedItem
		OpenSpinner(CMB_POSITION.cmbBox)
	Else If Result = xui.DialogResponse_Negative Then
		If SearchTemplate3.SearchField.Text = "" Then
			LABEL_LOAD_PERSON2.Text = "Assign Person"
		Else
			LABEL_LOAD_PERSON2.Text = SearchTemplate3.SearchField.Text.ToUpperCase
		End If
		CMB_POSITION.SelectedIndex = -1
		OpenSpinner(CMB_POSITION.cmbBox)
	Else
		LABEL_LOAD_PERSON2.Text = "Assign Person"
		CMB_POSITION.SelectedIndex = -1
		OpenSpinner(CMB_POSITION.cmbBox)
	End If
End Sub
Sub INPUT_PERSON1
	SearchTemplate.CustomListView1.Clear
	Dialog.Title = "Assign Person 1"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor1 = connection.ExecQuery("SELECT User FROM users_table WHERE (user <> 'ADMIN' and user <> '"&LABEL_LOAD_PERSON2.Text&"') ORDER BY user ASC")
	If cursor1.RowCount > 0 Then
	For i = 0 To cursor1.RowCount - 1
		Sleep(0)
		cursor1.Position = i
		Items.Add(cursor1.GetString("User"))
	Next
	SearchTemplate.SetItems(Items)
	Else
	End If
End Sub
Sub INPUT_PERSON2
	SearchTemplate3.CustomListView1.Refresh
	SearchTemplate3.CustomListView1.Clear
	Dialog.Title = "Assign Person 2"
	Dim Items2 As List
	Items2.Initialize
	Items2.Clear
	cursor2 = connection.ExecQuery("SELECT User FROM users_table WHERE (user <> 'ADMIN' and user <> '"&LABEL_LOAD_PERSON1.Text&"') ORDER BY user ASC")
	If cursor2.RowCount > 0 Then
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		Items2.Add(cursor2.GetString("User"))
	Next
	SearchTemplate3.SetItems(Items2)
	Else
	End If
End Sub
Sub CMB_POSITION_SelectedIndexChanged (Index As Int)
	SV_MONTHLY.ScrollToNow(53%x)
End Sub
#End Region

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
	LABEL_LOAD_BARCODE.Text = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
#Region Scan Barcode
	Dim add_query As String
	If MONTHLY_INVENTORY_MODULE.principal_id = "ALL" Then
		add_query = ""
	Else
		add_query = " AND principal_id = '"&MONTHLY_INVENTORY_MODULE.principal_id&"'"
	End If
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
		CLEAR_INPUT
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
		cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
		If cursor6.RowCount > 0 Then
			For row = 0 To cursor6.RowCount - 1
				cursor6.Position = row
				LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring("principal_name")
			Next
			EDITTEXT_QUANTITY.Text = ""
			reason = "N/A"
			input_type = "BARCODE"
			SV_MONTHLY.ScrollToNow(53%x)
			EDITTEXT_QUANTITY.RequestFocus
			Sleep(0)
			CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
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

#Region INPUT
Sub SWITCH_CONTINUES_ValueChanged (Value As Boolean)
	CONTINUES
End Sub
Sub CONTINUES
	If SWITCH_CONTINUES.Value = True Then
		ToastMessageShow("Continues mode : ON", False)
		LABEL_CONTINUES.TextColor = Colors.Green
	Else
		ToastMessageShow("Continues mode : OFF", False)
		LABEL_CONTINUES.TextColor = Colors.Red
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
#Region Find Product
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
				
				LABEL_LOAD_BARCODE.Text = cursor3.GetString("bar_code") & ("(PCS)")
		
			Next
			cursor6 = connection.ExecQuery("SELECT principal_name FROM principal_table WHERE principal_id ='" & principal_id & "'")
			If cursor6.RowCount > 0 Then
				For row = 0 To cursor6.RowCount - 1
					cursor6.Position = row
					LABEL_LOAD_PRINCIPAL.Text = cursor6.Getstring("principal_name")
				Next
				reason = ls.Get(Result2)
				input_type = "MANUAL"
				SV_MONTHLY.ScrollToNow(53%x)
				OpenSpinner(CMB_UNIT.cmbBox)
				CTRL.HideKeyboard
				Sleep(0)
				CMB_UNIT.SelectedIndex = -1
				scan_code = "N/A"
				
			End If
#End Region		
		End If
	End If
End Sub
Sub INPUT_MANUAL
	Dim principal_query As String
	If MONTHLY_INVENTORY_MODULE.principal_name = "ALL" Then
		principal_query = ""
	Else
		principal_query = "AND principal_id = '"&MONTHLY_INVENTORY_MODULE.principal_id&"'"
	End If
	Sleep(0)
	SearchTemplate2.CustomListView1.Clear
	Dialog.Title = "Find Product"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor2 = connection.ExecQuery("SELECT * FROM product_table WHERE prod_status = '0' and flag_deleted = '0' "&principal_query&" ORDER BY product_desc ASC")
	For i = 0 To cursor2.RowCount - 1
		Sleep(0)
		cursor2.Position = i
		Items.Add(cursor2.GetString("product_desc"))
	Next
	SearchTemplate2.SetItems(Items)
End Sub
Sub PRINCIPAL_SELECT
	If MONTHLY_INVENTORY_MODULE.principal_name = "ALL" Then
		LABEL_LOAD_PRINCIPAL.Text = ""
	Else
		LABEL_LOAD_PRINCIPAL.Text = MONTHLY_INVENTORY_MODULE.principal_name
	End If
End Sub
Sub CMB_UNIT_SelectedIndexChanged (Index As Int)
	EDITTEXT_QUANTITY.RequestFocus
	Sleep(0)
	CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
End Sub
Sub COUNT_TRANSACTION
	cursor6 = connection.ExecQuery("SELECT COUNT(transaction_number) as transaction_count from inventory_disc_table WHERE transaction_id = '" & MONTHLY_INVENTORY_MODULE.transaction_id & "'")
	For i = 0 To cursor6.RowCount - 1
		cursor6.Position = i
		If cursor6.GetString("transaction_count") = Null Or cursor6.GetString("transaction_count") = "" Then
			UpdateIcon("table", AddBadgeToIcon(tableBitmap, 0))
		Else
			UpdateIcon("table", AddBadgeToIcon(tableBitmap, cursor6.GetString("transaction_count")))
			Log(cursor6.GetString("transaction_count"))
		End If
	Next
End Sub
Sub BUTTON_ADD_Click
	Dim transaction_number As String
	Dim pass As String
	Dim input_number As String
	If LOGIN_MODULE.username <> "" Or LOGIN_MODULE.tab_id <> "" Then
	If LABEL_LOAD_PERSON1.Text = "Assign Person" And LABEL_LOAD_PERSON2.Text = "Assign Person" Then
		Msgbox2Async("Please assign atleast one person.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		SV_MONTHLY.ScrollToNow(9%x)
		OpenLabel(LABEL_LOAD_PERSON1)
	Else
		If LABEL_LOAD_DESCRIPTION.Text = "" Then
			Msgbox2Async("Scan or Choose a product first!", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			SV_MONTHLY.ScrollToNow(53%x)
		Else
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
#Region Pass
				cursor1 = connection.ExecQuery("SELECT * FROM inventory_disc_table WHERE transaction_id = '" & MONTHLY_INVENTORY_MODULE.transaction_id & "' and product_description = '"&LABEL_LOAD_DESCRIPTION.Text&"' and unit = '"&CMB_UNIT.cmbBox.SelectedItem&"'")
				If cursor1.RowCount > 0 Then
					For ir = 0 To cursor1.RowCount - 1
						cursor1.Position = ir
						input_number = input_number & " , " & cursor1.GetString("transaction_number")
					Next
					Msgbox2Async("The product you adding " & LABEL_LOAD_DESCRIPTION.Text & " has existing transaction at number(s)" & input_number.SubString(3) & " the same unit of "&CMB_UNIT.cmbBox.SelectedItem&"" & CRLF & "Do you want to add this another transaction?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					Wait For Msgbox_Result (Result As Int)
					If Result = DialogResponse.POSITIVE Then
						pass = 1
					Else
						pass = 0
					End If
				Else
					pass = 1
				End If
				
#End Region				
				If pass = 1 Then
					
					If CMB_INVTYPE.cmbBox.SelectedIndex = -1 Then
						CMB_INVTYPE.cmbBox.SelectedIndex = 0
					End If
					
					If CMB_POSITION.cmbBox.SelectedIndex = -1 Then
						CMB_POSITION.cmbBox.SelectedIndex = 0
					End If
					
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
				
					cursor2 = connection.ExecQuery("SELECT MAX(CAST(transaction_number as INT)) as transaction_number from inventory_disc_table WHERE transaction_id = '" & MONTHLY_INVENTORY_MODULE.transaction_id & "'")
					For i = 0 To cursor2.RowCount - 1
						cursor2.Position = i
						If cursor2.GetString("transaction_number") = Null Or cursor2.GetString("transaction_number") = "" Then
							transaction_number =1
						Else
							transaction_number = cursor2.GetString("transaction_number") + 1
						End If
					Next
					
					If CMB_INVTYPE.cmbBox.SelectedIndex = CMB_INVTYPE.cmbBox.IndexOf("COUNT") Then
						Dim query As String = "INSERT INTO inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
						connection.ExecNonQuery2(query,Array As String(MONTHLY_INVENTORY_MODULE.transaction_id,transaction_number,principal_id, _
					LABEL_LOAD_PRINCIPAL.Text,product_id,LABEL_LOAD_VARIANT.Text,LABEL_LOAD_DESCRIPTION.Text,CMB_UNIT.cmbBox.SelectedItem, _
					EDITTEXT_QUANTITY.Text,total_pieces,CMB_POSITION.cmbBox.SelectedItem,input_type,reason,scan_code,CMB_INVTYPE.cmbBox.SelectedItem,LOGIN_MODULE.username, _
					LABEL_LOAD_PERSON1.Text,LABEL_LOAD_PERSON2.Text,"-","-","-",DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now), _
					"0",LOGIN_MODULE.tab_id))
					Else
						Dim query As String = "INSERT INTO inventory_disc_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
						connection.ExecNonQuery2(query,Array As String(MONTHLY_INVENTORY_MODULE.transaction_id,transaction_number,principal_id, _
					LABEL_LOAD_PRINCIPAL.Text,product_id,LABEL_LOAD_VARIANT.Text,LABEL_LOAD_DESCRIPTION.Text,CMB_UNIT.cmbBox.SelectedItem, _
					EDITTEXT_QUANTITY.Text,total_pieces,CMB_POSITION.cmbBox.SelectedItem,input_type,reason,scan_code,CMB_INVTYPE.cmbBox.SelectedItem,LOGIN_MODULE.username, _
					LABEL_LOAD_PERSON1.Text,LABEL_LOAD_PERSON2.Text,LOGIN_MODULE.username,LABEL_LOAD_PERSON1.Text,LABEL_LOAD_PERSON2.Text,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now), _
					"0",LOGIN_MODULE.tab_id))
					End If
					Dim unit As String = CMB_UNIT.cmbBox.SelectedItem
					If unit = "PCS" Then
						If EDITTEXT_QUANTITY.Text > 1 Then
							unit = "PIECES"
						Else
							unit = "PIECE"
						End If
					Else If unit = "DOZ" Then
						unit = "DOZEN"
					End If
					Sleep(0)
					TTS1.Speak(EDITTEXT_QUANTITY.Text & " " & unit & " ADDED." , True)
					Sleep(0)
					COUNT_TRANSACTION
					ToastMessageShow("Product Added", False)
					
					If SWITCH_CONTINUES.Value = True Then
#Region Continues True
						If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
							caseper = 0
						Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
							pcsper = 0
						Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
							boxper = 0
						Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
							dozper = 0
						Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
							bagper = 0
						Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
							packper = 0
						End If
						CMB_UNIT.cmbBox.Clear
						If caseper > 0 Then
							CMB_UNIT.cmbBox.Add("CASE")
						End If
						If pcsper > 0 Then
							CMB_UNIT.cmbBox.Add("PCS")
						End If
						If boxper > 0 Then
							CMB_UNIT.cmbBox.Add("BOX")
						End If
						If dozper > 0 Then
							CMB_UNIT.cmbBox.Add("DOZ")
						End If
						If bagper > 0 Then
							CMB_UNIT.cmbBox.Add("BAG")
						End If
						If packper > 0 Then
							CMB_UNIT.cmbBox.Add("PACK")
						End If
						If 	caseper = 0 And pcsper = 0 And boxper = 0 And dozper = 0 And packper = 0 And bagper = 0 Then
							CLEAR_INPUT
							ToastMessageShow("No unit remaining", False)
						Else
							Sleep(0)
							CMB_UNIT.SelectedIndex = 0
							EDITTEXT_QUANTITY.Text = ""
						End If
						Sleep(0)
#End Region
					Else
						CLEAR_INPUT
					End If
				End If
			End If
		End If
		End If
	Else
		Msgbox2Async("TABLET ID AND USERNAME CANNOT READ BY THE SYSTEM, PLEASE RE-LOGIN AGAIN.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
End Sub
Sub CLEAR_INPUT
	LABEL_LOAD_BARCODE.Text = ""
	LABEL_LOAD_DESCRIPTION.Text = ""
	LABEL_LOAD_VARIANT.Text = ""
	LABEL_LOAD_PRINCIPAL.Text = ""
	CMB_UNIT.cmbBox.Clear
	EDITTEXT_QUANTITY.Text = ""
	PRINCIPAL_SELECT
End Sub
Sub BUTTON_CLEAR_Click
	CLEAR_INPUT
End Sub
#End Region

Sub TTS1_Ready (Success As Boolean)
	If Success Then
		'enable all views
'		Cell_Click 'play first sentence
	Else
		ToastMessageShow("Error initializing TTS engine.", False)
	End If
End Sub

Sub BUTTON_SEE_TABLE_Click
	If CMB_INVTYPE.cmbBox.SelectedIndex = -1 Then CMB_INVTYPE.cmbBox.SelectedIndex = 0
	
	If LABEL_LOAD_PERSON1.Text = "Assign Person" And LABEL_LOAD_PERSON2.Text = "Assign Person" And CMB_INVTYPE.cmbBox.SelectedIndex = CMB_INVTYPE.cmbBox.IndexOf("RECOUNT") Then
		Msgbox2Async("Please assign atleast one person.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		SV_MONTHLY.ScrollToNow(9%x)
	Else
		CLEAR_INPUT
		
		assign1 = LABEL_LOAD_PERSON1.Text
		assign2 = LABEL_LOAD_PERSON2.Text
		
		inventory_type = CMB_INVTYPE.cmbBox.SelectedItem
	
		StartActivity(INVENTORY_TABLE)
'		SetAnimation("right_to_center", "center_to_left")
	End If
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
	StartActivity(MONTHLY_INVENTORY_MODULE)
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
	If Item.Title = "table" Then
		If CMB_INVTYPE.cmbBox.SelectedIndex = -1 Then CMB_INVTYPE.cmbBox.SelectedIndex = 0
	
		If LABEL_LOAD_PERSON1.Text = "Assign Person" And LABEL_LOAD_PERSON2.Text = "Assign Person" And CMB_INVTYPE.cmbBox.SelectedIndex = CMB_INVTYPE.cmbBox.IndexOf("RECOUNT") Then
			Msgbox2Async("Please assign atleast one person.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			SV_MONTHLY.ScrollToNow(9%x)
		Else
			CLEAR_INPUT
		
			assign1 = LABEL_LOAD_PERSON1.Text
			assign2 = LABEL_LOAD_PERSON2.Text
			inventory_type = CMB_INVTYPE.cmbBox.SelectedItem
			
	
			StartActivity(INVENTORY_TABLE)
'		SetAnimation("right_to_center", "center_to_left")
		End If
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
	Else If Item.Title = "calcu" Then
		oncal
		PANEL_BG_CALCU.SetVisibleAnimated(300,True)
		PANEL_BG_CALCU.BringToFront
		LABEL_LOAD_ANSWER.Text = "0"
	End If
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
	SV_MONTHLY.ScrollToNow(53%x)
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
#End Region

Sub BUTTON_EXIT_CALCU_Click
	PANEL_BG_CALCU.SetVisibleAnimated(300,False)
	EDITTEXT_QUANTITY.Text = 0
End Sub
Sub PANEL_BG_CALCU_Click
	Return True
End Sub

Sub EDITTEXT_QUANTITY_EnterPressed
	OpenButton(BUTTON_ADD)
End Sub

Sub OpenButton(bt As Label)
	Dim reflect As Reflector
	reflect.Target = bt
	reflect.RunMethod("performClick")
End Sub