B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=10
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
	
	'bluetooth
	Dim serial1 As Serial
	Dim AStream As AsyncStreams
	Dim Ts As Timer
	
	'FORMULA
	Dim BOOKING_PCS As String
	Dim BOOKING_CASE As String
	Dim BOOKING_DOZ As String
	Dim BOOKING_BOX As String
	Dim BOOKING_PACK As String
	Dim BOOKING_BAG As String
	Dim EXTRUCK_PCS As String
	Dim EXTRUCK_CASE As String
	Dim EXTRUCK_DOZ As String
	Dim EXTRUCK_BOX As String
	Dim EXTRUCK_PACK As String
	Dim EXTRUCK_BAG As String
	
	Dim cartBitmap As Bitmap
	Dim addBitmap As Bitmap
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	'ime
	Dim CTRL As IME
	
	'bluetooth
	Dim ScannerMacAddress As String
	Dim ScannerOnceConnected As Boolean
	
	Private cvs As B4XCanvas
	Private xui As XUI
	
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private SearchTemplate As B4XSearchTemplate
	Private SearchTemplate As B4XSearchTemplate
	Private SearchTemplate3 As B4XSearchTemplate
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	Private CMB_PRICETYPE As B4XComboBox
	Private LABEL_LOAD_VARIANT As Label
	Private LABEL_LOAD_DESCRIPTION As Label
	Private LABEL_LOAD_PER_PCS As Label
	Private LABEL_LOAD_PER_CASE As Label
	Private LABEL_LOAD_PER_DOZ As Label
	Private LABEL_LOAD_PER_BOX As Label
	Private LABEL_LOAD_PER_PACK As Label
	Private LABEL_LOAD_PER_BAG As Label
	Private CMB_UNIT As B4XComboBox
	Private EDITTEXT_QUANTITY As EditText
	Private LABEL_LOAD_TOTPRICE As Label
	Private IMG_PRODUCT As ImageView
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
	Activity.LoadLayout("price_check")

	cartBitmap = LoadBitmap(File.DirAssets, "bluetooth_notconnected.png")
	addBitmap = LoadBitmap(File.DirAssets, "pencil.png")
	
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
#End Region

	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	'spinner
	CMB_PRICETYPE.cmbBox.Add("BOOKING")
	CMB_PRICETYPE.cmbBox.Add("EXTRUCK")
	
	CLEAR_ALL
	
	Sleep(0)
	Dim Ref As Reflector
	Ref.Target = EDITTEXT_QUANTITY ' The text field being referenced
	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Transparent)
	EDITTEXT_QUANTITY.Background = bg
	Sleep(0)
	INPUT_MANUAL
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "cart", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("cart", cartBitmap)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "type product", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("type product", addBitmap)
	Sleep(0)
End Sub

Sub Activity_Resume
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
	Else If Item.Title = "type product" Then
		MANUAL_SEARCH
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
			DateTime.DateFormat = "yyyy-MM-dd"
			trigger = 0
		Next
	Else
		trigger = 1
		Msgbox2Async("The barcode you scanned is not REGISTERED IN THE SYSTEM.", "Warning", "Ok", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		CLEAR_ALL
	End If
	If trigger = 0 Then
		cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&LABEL_LOAD_DESCRIPTION.Text&"'")
		For qrow = 0 To cursor3.RowCount - 1
			cursor3.Position = qrow
			LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
			
			Dim Buffer() As Byte 'declare an empty byte array
			Buffer = cursor1.GetBlob("Picture")
			Dim InputStream1 As InputStream
			InputStream1.InitializeFromBytesArray(Buffer, 0, Buffer.Length)
  
			Dim Bitmap1 As Bitmap
			Bitmap1.Initialize2(InputStream1)
			InputStream1.Close
			IMG_PRODUCT.Bitmap = Bitmap1
			
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
			CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE")
			Sleep(0)
						
			BOOKING_PCS = Number.Format3((cursor3.GetString("PCS_BOOKING")),0,2,2,".","","",0,15)
			BOOKING_CASE = Number.Format3((cursor3.GetString("CASE_BOOKING")),0,2,2,".","","",0,15)
			BOOKING_DOZ = Number.Format3((cursor3.GetString("DOZ_BOOKING")),0,2,2,".","","",0,15)
			BOOKING_BOX = Number.Format3((cursor3.GetString("BOX_BOOKING")),0,2,2,".","","",0,15)
			BOOKING_PACK = Number.Format3((cursor3.GetString("PACK_BOOKING")),0,2,2,".","","",0,15)
			BOOKING_BAG = Number.Format3((cursor3.GetString("BAG_BOOKING")),0,2,2,".","","",0,15)
			EXTRUCK_PCS = Number.Format3((cursor3.GetString("PCS_EXTRUCK")),0,2,2,".","","",0,15)
			EXTRUCK_CASE = Number.Format3((cursor3.GetString("CASE_EXTRUCK")),0,2,2,".","","",0,15)
			EXTRUCK_DOZ = Number.Format3((cursor3.GetString("DOZ_EXTRUCK")),0,2,2,".","","",0,15)
			EXTRUCK_BOX = Number.Format3((cursor3.GetString("BOX_EXTRUCK")),0,2,2,".","","",0,15)
			EXTRUCK_PACK = Number.Format3((cursor3.GetString("PACK_EXTRUCK")),0,2,2,".","","",0,15)
			EXTRUCK_BAG = Number.Format3((cursor3.GetString("BAG_EXTRUCK")),0,2,2,".","","",0,15)
			
			If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PRICETYPE.cmbBox.IndexOf("BOOKING") Then
				LABEL_LOAD_PER_PCS.Text = "₱"&Number.Format3((BOOKING_PCS),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_CASE.Text = "₱"&Number.Format3((BOOKING_CASE),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_DOZ.Text = "₱"&Number.Format3((BOOKING_DOZ),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_BOX.Text = "₱"&Number.Format3((BOOKING_BOX),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_PACK.Text = "₱"&Number.Format3((BOOKING_PACK),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_BAG.Text = "₱"&Number.Format3((BOOKING_BAG),0,2,2,".",",","",0,15)
			Else
				LABEL_LOAD_PER_PCS.Text = "₱"& Number.Format3((EXTRUCK_PCS),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_CASE.Text = "₱"&Number.Format3((EXTRUCK_CASE),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_DOZ.Text = "₱"&Number.Format3((EXTRUCK_DOZ),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_BOX.Text = "₱"&Number.Format3((EXTRUCK_BOX),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_PACK.Text = "₱"&Number.Format3((EXTRUCK_PACK),0,2,2,".",",","",0,15)
				LABEL_LOAD_PER_BAG.Text = "₱"&Number.Format3((EXTRUCK_BAG),0,2,2,".",",","",0,15)
			End If
			Sleep(0)
			EDITTEXT_QUANTITY.Text = "0"
			EDITTEXT_QUANTITY.RequestFocus
			Sleep(0)
			CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
			EDITTEXT_QUANTITY.SelectAll
			FORMULA
							
		Next
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

Sub CMB_PRICETYPE_SelectedIndexChanged (Index As Int)
	If LABEL_LOAD_VARIANT.text <> "-" Then
		If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PRICETYPE.cmbBox.IndexOf("BOOKING") Then
			LABEL_LOAD_PER_PCS.Text = "₱"&Number.Format3((BOOKING_PCS),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_CASE.Text = "₱"&Number.Format3((BOOKING_CASE),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_DOZ.Text = "₱"&Number.Format3((BOOKING_DOZ),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_BOX.Text = "₱"&Number.Format3((BOOKING_BOX),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_PACK.Text = "₱"&Number.Format3((BOOKING_PACK),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_BAG.Text = "₱"&Number.Format3((BOOKING_BAG),0,2,2,".",",","",0,15)
		Else
			LABEL_LOAD_PER_PCS.Text = "₱"& Number.Format3((EXTRUCK_PCS),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_CASE.Text = "₱"&Number.Format3((EXTRUCK_CASE),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_DOZ.Text = "₱"&Number.Format3((EXTRUCK_DOZ),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_BOX.Text = "₱"&Number.Format3((EXTRUCK_BOX),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_PACK.Text = "₱"&Number.Format3((EXTRUCK_PACK),0,2,2,".",",","",0,15)
			LABEL_LOAD_PER_BAG.Text = "₱"&Number.Format3((EXTRUCK_BAG),0,2,2,".",",","",0,15)
		End If
	End If
End Sub

Sub CLEAR_ALL
	LABEL_LOAD_VARIANT.Text = "-"
	LABEL_LOAD_DESCRIPTION.Text = "-"
	LABEL_LOAD_PER_PCS.Text = "-"
	LABEL_LOAD_PER_CASE.Text = "-"
	LABEL_LOAD_PER_DOZ.Text = "-"
	LABEL_LOAD_PER_BOX.Text = "-"
	LABEL_LOAD_PER_PACK.Text = "-"
	LABEL_LOAD_PER_BAG.Text = "-"
End Sub

Sub FORMULA
	If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("") Then
		
	Else
		If EDITTEXT_QUANTITY.Text = "" Then
			LABEL_LOAD_TOTPRICE.Text = "₱.00"
		Else
			If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PRICETYPE.cmbBox.IndexOf("BOOKING") Then
				If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((BOOKING_PCS * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((BOOKING_CASE * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((BOOKING_BOX * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((BOOKING_DOZ * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((BOOKING_PACK * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((BOOKING_BAG * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				End If
			Else
				If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PCS") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((EXTRUCK_PCS * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((EXTRUCK_CASE * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BOX") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((EXTRUCK_BOX * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("DOZ") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((EXTRUCK_DOZ * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("PACK") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((EXTRUCK_PACK * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				Else If CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("BAG") Then
					LABEL_LOAD_TOTPRICE.Text = "₱"&Number.Format3((EXTRUCK_BAG * EDITTEXT_QUANTITY.Text),0,2,2,".",",","",0,15)
				End If
			End If
		End If
	End If
End Sub

Sub EDITTEXT_QUANTITY_TextChanged (Old As String, New As String)
	FORMULA
End Sub

Sub MANUAL_SEARCH
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
		Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
		Wait For (rs) Complete (Result As Int)
		If Result = xui.DialogResponse_Positive Then
#Region Find Product
			cursor3 = connection.ExecQuery("SELECT * FROM product_table WHERE product_desc ='"&SearchTemplate.SelectedItem&"'")
			For qrow = 0 To cursor3.RowCount - 1
				cursor3.Position = qrow
				LABEL_LOAD_VARIANT.Text = cursor3.GetString("product_variant")
				LABEL_LOAD_DESCRIPTION.Text = cursor3.GetString("product_desc")

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
				CMB_UNIT.cmbBox.SelectedIndex = CMB_UNIT.cmbBox.IndexOf("CASE")
				Sleep(0)
						
				BOOKING_PCS = Number.Format3((cursor3.GetString("PCS_BOOKING")),0,2,2,".","","",0,15)
				BOOKING_CASE = Number.Format3((cursor3.GetString("CASE_BOOKING")),0,2,2,".","","",0,15)
				BOOKING_DOZ = Number.Format3((cursor3.GetString("DOZ_BOOKING")),0,2,2,".","","",0,15)
				BOOKING_BOX = Number.Format3((cursor3.GetString("BOX_BOOKING")),0,2,2,".","","",0,15)
				BOOKING_PACK = Number.Format3((cursor3.GetString("PACK_BOOKING")),0,2,2,".","","",0,15)
				BOOKING_BAG = Number.Format3((cursor3.GetString("BAG_BOOKING")),0,2,2,".","","",0,15)
				EXTRUCK_PCS = Number.Format3((cursor3.GetString("PCS_EXTRUCK")),0,2,2,".","","",0,15)
				EXTRUCK_CASE = Number.Format3((cursor3.GetString("CASE_EXTRUCK")),0,2,2,".","","",0,15)
				EXTRUCK_DOZ = Number.Format3((cursor3.GetString("DOZ_EXTRUCK")),0,2,2,".","","",0,15)
				EXTRUCK_BOX = Number.Format3((cursor3.GetString("BOX_EXTRUCK")),0,2,2,".","","",0,15)
				EXTRUCK_PACK = Number.Format3((cursor3.GetString("PACK_EXTRUCK")),0,2,2,".","","",0,15)
				EXTRUCK_BAG = Number.Format3((cursor3.GetString("BAG_EXTRUCK")),0,2,2,".","","",0,15)
			
				If CMB_PRICETYPE.cmbBox.SelectedIndex = CMB_PRICETYPE.cmbBox.IndexOf("BOOKING") Then
					LABEL_LOAD_PER_PCS.Text = "₱"&Number.Format3((BOOKING_PCS),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_CASE.Text = "₱"&Number.Format3((BOOKING_CASE),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_DOZ.Text = "₱"&Number.Format3((BOOKING_DOZ),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_BOX.Text = "₱"&Number.Format3((BOOKING_BOX),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_PACK.Text = "₱"&Number.Format3((BOOKING_PACK),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_BAG.Text = "₱"&Number.Format3((BOOKING_BAG),0,2,2,".",",","",0,15)
				Else
					LABEL_LOAD_PER_PCS.Text = "₱"& Number.Format3((EXTRUCK_PCS),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_CASE.Text = "₱"&Number.Format3((EXTRUCK_CASE),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_DOZ.Text = "₱"&Number.Format3((EXTRUCK_DOZ),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_BOX.Text = "₱"&Number.Format3((EXTRUCK_BOX),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_PACK.Text = "₱"&Number.Format3((EXTRUCK_PACK),0,2,2,".",",","",0,15)
					LABEL_LOAD_PER_BAG.Text = "₱"&Number.Format3((EXTRUCK_BAG),0,2,2,".",",","",0,15)
				End If

			Next
			Sleep(0)
			EDITTEXT_QUANTITY.Text = "0"
			EDITTEXT_QUANTITY.RequestFocus
			Sleep(0)
			CTRL.ShowKeyboard(EDITTEXT_QUANTITY)
			EDITTEXT_QUANTITY.SelectAll
			FORMULA
		End If
#End Region		
	End If
End Sub
Sub INPUT_MANUAL
	Sleep(0)
	SearchTemplate.CustomListView1.Clear
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
	SearchTemplate.SetItems(Items)
End Sub