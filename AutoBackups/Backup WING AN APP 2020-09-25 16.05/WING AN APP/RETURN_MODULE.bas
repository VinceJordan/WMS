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
	
	Dim cartBitmap As Bitmap
	Dim addBitmap As Bitmap
	
	'string
	Dim picklist_id As String
	Dim date_dispatch As String
	Dim date_return As String
	Dim route_name As String
	Dim plate_no As String
	Dim transaction_type As String
	Dim return_route_id As String
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	'ime
	Dim CTRL As IME
	
		
	Private cvs As B4XCanvas
	Private xui As XUI
	
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private DateTemplate As B4XDateTemplate
	Private SearchTemplate As B4XSearchTemplate
	Private InputTemplate As B4XInputTemplate
	
	Private XSelections As B4XTableSelections
	Private NameColumn(5) As B4XTableColumn
		
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight

	Private PANEL_BG_TYPE As Panel
	Private EDITTEXT_TYPE As EditText
	Private BUTTON_EXIT_ROUTE As Button
	Private LABEL_MSGBOX2 As Label
	Private LABEL_MSGBOX1 As Label
	Private PANEL_BG_MSGBOX As Panel
	Private LABEL_HEADER_TEXT As Label
	Private LABEL_LOAD_HELPER As Label
	Private LABEL_LOAD_DRIVER As Label
	Private LABEL_LOAD_PLATE As Label
	Private LABEL_LOAD_ROUTE_NAME As Label
	Private LVL_PICKLIST As ListView
	Private PANEL_ROUTE As Panel
	Private PANEL_BG_ROUTE As Panel
	Private LABEL_LOAD_HELPER2 As Label
	Private LABEL_LOAD_HELPER3 As Label
	Private LABEL_LOAD_DATE As Label
	Private LABEL_LOAD_TIME As Label
	Private TABLE_ROUTE As B4XTable
	Private BUTTON_DELIVERED As Button
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
	Activity.LoadLayout("return")

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

	ACToolBarLight1.Elevation = 1dip

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

	SearchTemplate.Initialize
	SearchTemplate.CustomListView1.DefaultTextBackgroundColor = Colors.White
	SearchTemplate.CustomListView1.DefaultTextColor = Colors.Black
	SearchTemplate.SearchField.TextField.TextColor = Colors.Black
	SearchTemplate.ItemHightlightColor = Colors.White
	SearchTemplate.TextHighlightColor = Colors.RGB(82,169,255)
	
	InputTemplate.Initialize
	InputTemplate.SetBorderColor(Colors.Green,Colors.RGB(215,215,215))
	InputTemplate.TextField1.TextColor = Colors.Black
	
#End Region

	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.White, 5, 1, Colors.RGB(215,215,215))
	EDITTEXT_TYPE.Background = bg


	LOAD_ROUTE_HEADER
	Sleep(0)
	LOAD_ROUTE
'	Sleep(0)
'	Dim Ref As Reflector
'	Ref.Target = EDITTEXT_QUANTITY ' The text field being referenced
'	Ref.RunMethod2("setImeOptions", 268435456, "java.lang.int") 'IME_FLAG_NO_EXTRACT_UI
	
'	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.RGB(82,169,255),5,0,Colors.Transparent)
'	EDITTEXT_QUANTITY.Background = bg
	Sleep(0)
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "Load Picklist", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Load Picklist", addBitmap)
	Sleep(0)
	
End Sub

Sub Activity_Resume
	LOAD_ROUTE
End Sub
Sub Activity_Pause (UserClosed As Boolean)

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
	If Item.Title = "Load Picklist" Then
		PANEL_BG_TYPE.SetVisibleAnimated(300,True)
		PANEL_BG_TYPE.BringToFront
		EDITTEXT_TYPE.Text = ""
		EDITTEXT_TYPE.RequestFocus
		CTRL.ShowKeyboard(EDITTEXT_TYPE)
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
Sub VALIDATE_PICKLIST_STATUS
	PANEL_BG_TYPE.SetVisibleAnimated(300,False)
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
	PANEL_BG_MSGBOX.BringToFront
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
				If row(res.Columns.Get("PickListStatus")) = "DISPATCH" Then
					GET_ROUTE
				Else if row(res.Columns.Get("PickListStatus")) = "DELIVERED" Then
					Log(98)
					Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")),  _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				Else
					Msgbox2Async("The picklist you scan :" & CRLF & _
						" Picklist Name : " & row(res.Columns.Get("PicklistName")) & CRLF & _
						" is on :" & row(res.Columns.Get("PickListStatus")) & CRLF & _
						" Tablet : TABLET " & row(res.Columns.Get("PreparingTabletid")), _
			   			"Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				End If
			Next
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			Msgbox2Async("The Picklist ID you type/scan is not existing in the system. Please double check your Picklist ID.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub GET_ROUTE
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_route", Array As String(picklist_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
'		'work with result
		Log(2)
		If res.Rows.Size > 0 Then
			CLEAR_ROUTE
			For Each row() As Object In res.Rows
				date_dispatch = row(res.Columns.Get("date_dispatch"))
				LABEL_LOAD_ROUTE_NAME.Text = row(res.Columns.Get("route_name"))
				LABEL_LOAD_PLATE.Text = row(res.Columns.Get("plate_no"))
				LABEL_LOAD_DRIVER.Text = row(res.Columns.Get("driver"))
				LABEL_LOAD_HELPER.Text = row(res.Columns.Get("helper1"))
				LABEL_LOAD_HELPER2.Text = row(res.Columns.Get("helper2"))
				LABEL_LOAD_HELPER3.Text = row(res.Columns.Get("helper3"))
				Sleep(0)
			Next
			Sleep(0)
			GET_PICK
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			Msgbox2Async("Picklist Route is empty, Please advice IT for this conflict.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub GET_PICK
	LVL_PICKLIST.Clear
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_carried_pick", Array As String(LABEL_LOAD_ROUTE_NAME.Text,date_dispatch,LABEL_LOAD_PLATE.Text))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
		For Each row() As Object In res.Rows
				LVL_PICKLIST.AddTwoLines2(row(res.Columns.Get("picklist_name")),row(res.Columns.Get("picklist_id")),row(res.Columns.Get("picklist_id")) & "|" & row(res.Columns.Get("picklist_name")))
			Sleep(0)
		Next
		SHOW_ROUTE
		Else
			PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
			Msgbox2Async("Picklist Route is empty, Please advice IT for this conflict.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		End If
		
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
#End Region


Sub LABEL_LOAD_DATE_Click
	Dialog.Title = "Select Returned Date"
	Wait For (Dialog.ShowTemplate(DateTemplate, "", "", "NOW")) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
	LABEL_LOAD_DATE.Text = DateTime.Date(DateTemplate.Date)
	OpenLabel(LABEL_LOAD_TIME)
	Else
	LABEL_LOAD_DATE.Text = DateTime.Date(DateTime.Now)
	OpenLabel(LABEL_LOAD_TIME)
	End If
End Sub
Sub LABEL_LOAD_TIME_Click
	InputTemplate.lblTitle.Text = "Enter Returned Time (HH:MM tt)"
	InputTemplate.RegexPattern = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9] [A,P,p,a][M,m]$" '<---optional. Remove if not needed
	Wait For (Dialog.ShowTemplate(InputTemplate, "OK", "", "NOW")) Complete (Result As Int)
	Dialog.Base.Top = 40%y - Dialog.Base.Height / 2
	If Result = xui.DialogResponse_Positive Then
		LABEL_LOAD_TIME.Text = InputTemplate.Text
	Else
		LABEL_LOAD_TIME.Text = DateTime.Time(DateTime.Now)
	End If
End Sub

Sub BUTTON_DELIVERED_Click
	If LABEL_LOAD_DATE.Text = "" Then
		ToastMessageShow("Please input a date returned.", False)
	Else If LABEL_LOAD_TIME.Text = "" Then
		ToastMessageShow("Please input a time returned", False)
	Else
		If BUTTON_DELIVERED.Text = "EDIT" Then
			LABEL_LOAD_DATE.Enabled = True
			LABEL_LOAD_TIME.Enabled = True
			OpenLabel(LABEL_LOAD_DATE)
			
			Dim bg2 As ColorDrawable
			bg2.Initialize2(Colors.ARGB(255,255,0,242), 5, 0, Colors.LightGray)
			BUTTON_DELIVERED.Background = bg2
			BUTTON_DELIVERED.Text = "DELIVERED"
			
			transaction_type = "NEW"
		Else
			Dim picklist_details As String
			Msgbox2Async("Are you sure you mark as delivered this route?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				NEW_TRANSACTION
				Sleep(0)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
				PANEL_BG_MSGBOX.BringToFront
				LABEL_HEADER_TEXT.Text = "Uploading Picklist"
				LABEL_MSGBOX2.Text = "Finalizing data..."
				For i = 0 To LVL_PICKLIST.Size - 1
					picklist_details = LVL_PICKLIST.GetItem(i)
					picklist_id = picklist_details.SubString2(0,picklist_details.IndexOf("|"))
					
					Log(picklist_id)
					connection.ExecNonQuery("DELETE FROM picklist_return_ref_table WHERE picklist_id = '"&picklist_id&"'")
					Dim QUERY As String = "insert into picklist_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
					connection.ExecNonQuery2(QUERY,Array As String(return_route_id,picklist_details.SubString2(0,picklist_details.IndexOf("|")),picklist_details.SubString(picklist_details.IndexOf("|")+1),LABEL_LOAD_ROUTE_NAME.Text,LABEL_LOAD_PLATE.Text _
					,LABEL_LOAD_DRIVER.Text,LABEL_LOAD_HELPER.Text,LABEL_LOAD_HELPER2.Text,LABEL_LOAD_HELPER3.Text,LABEL_LOAD_DATE.Text,LABEL_LOAD_TIME.Text,DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now),LOGIN_MODULE.username,LOGIN_MODULE.tab_id))
					ToastMessageShow("Added Successfully",False)
					Sleep(0)
					Dim cmd As DBCommand = CreateCommand("update_return", Array As String(LABEL_LOAD_DATE.Text,LABEL_LOAD_TIME.Text,return_route_id ,LOGIN_MODULE.username,LOGIN_MODULE.tab_id, picklist_id))
					Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
					Wait For(js) JobDone(js As HttpJob)
					If js.Success Then
						UPDATE_DELIVERED
					Else
						PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
						Log("ERROR: " & js.ErrorMessage)
						Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						ToastMessageShow("Updating Error", False)
					End If
					js.Release
				Next
				PANEL_BG_ROUTE.SetVisibleAnimated(300,False)
				Sleep(0)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				ToastMessageShow("Route mark as delivered.", False)
				LOAD_ROUTE
			End If
		End If
	End If
End Sub
Sub UPDATE_DELIVERED
	Dim cmd As DBCommand = CreateCommand("update_picklist_delivered", Array(picklist_id.Trim))
	Dim jr As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
	Wait For(jr) JobDone(jr As HttpJob)
	If jr.Success Then
		
	Else
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Log("ERROR: " & jr.ErrorMessage)
		Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub

Sub CLEAR_ROUTE
	LABEL_LOAD_ROUTE_NAME.Text = ""
	LABEL_LOAD_PLATE.Text = ""
	LABEL_LOAD_DRIVER.Text = ""
	LABEL_LOAD_HELPER.Text = ""
	LABEL_LOAD_HELPER2.text = ""
	LABEL_LOAD_HELPER3.Text = ""
	LABEL_LOAD_DATE.Text = ""
	LABEL_LOAD_TIME.Text = ""
End Sub
Sub BUTTON_EXIT_ROUTE_Click
	PANEL_BG_ROUTE.SetVisibleAnimated(300,False)
End Sub
Sub NEW_TRANSACTION
	Dim CExpDate As String = DateTime.GetYear(DateTime.Now)
	Dim Cxmo As String = DateTime.GetMonth(DateTime.Now)
	Dim Cxday As String = DateTime.GetDayOfMonth(DateTime.Now)
	Dim Cxtime As String = DateTime.GetHour(DateTime.Now)
	Dim Cxmin As String = DateTime.GetMinute(DateTime.Now)
	Dim Cxsecs As String = DateTime.GetSecond(DateTime.Now)
	Sleep(0)
	return_route_id = "RRID"&CExpDate&Cxmo&Cxday&Cxtime&Cxmin&Cxsecs&LOGIN_MODULE.tab_id
	Log(return_route_id)
	ToastMessageShow("Transaction Created",False)
End Sub



Sub LOAD_ROUTE_HEADER
	NameColumn(0)=TABLE_ROUTE.AddColumn("Date Return", TABLE_ROUTE.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_ROUTE.AddColumn("Route", TABLE_ROUTE.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_ROUTE.AddColumn("Truck", TABLE_ROUTE.COLUMN_TYPE_TEXT)
	NameColumn(3)=TABLE_ROUTE.AddColumn("Carried Picklist", TABLE_ROUTE.COLUMN_TYPE_TEXT)
	NameColumn(4)=TABLE_ROUTE.AddColumn("User", TABLE_ROUTE.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_ROUTE
	Dim picklist_carried As String
	Sleep(0)
	ProgressDialogShow2("Loading...", False)
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor5 = connection.ExecQuery("SELECT * FROM picklist_return_ref_table GROUP BY return_route_id ORDER BY date_return DESC")
	If cursor5.RowCount > 0 Then
		For ia = 0 To cursor5.RowCount - 1
			Sleep(10)
			cursor5.Position = ia
			Dim row(5) As Object
			cursor4 = connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE return_route_id = '"&cursor5.GetString("return_route_id")&"' GROUP BY picklist_id")
			If cursor4.RowCount > 0 Then
				picklist_carried = ""
				For ic = 0 To cursor4.RowCount - 1
					cursor4.Position = ic
					Sleep(10)
					picklist_carried = picklist_carried & ", " & cursor4.GetString("picklist_id")
				Next
				row(0) = cursor5.GetString("date_return")
				row(1) = cursor5.GetString("route_name")
				row(2) = cursor5.GetString("plate_no")
				row(3) = picklist_carried.SubString2(2,picklist_carried.Length)
				row(4) = cursor5.GetString("user_info")
				Data.Add(row)
			End If
		Next
		TABLE_ROUTE.NumberOfFrozenColumns = 1
		TABLE_ROUTE.RowHeight = 50dip
		Sleep(100)
'		GET_STATUS
		Sleep(0)
		ProgressDialogHide
	Else
		ProgressDialogHide
		ToastMessageShow("Invoice is empty", False)
	End If
	TABLE_ROUTE.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_ROUTE)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_ROUTE_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	Dim RowData As Map = TABLE_ROUTE.GetRow(RowId)
End Sub
Sub TABLE_ROUTE_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
	
	Dim RowData As Map = TABLE_ROUTE.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)
	CLEAR_ROUTE
	LVL_PICKLIST.Clear 
	cursor6 = connection.ExecQuery("SELECT * FROM picklist_return_ref_table WHERE date_return = '"&RowData.Get("Date Return")&"' AND route_name = '"&RowData.Get("Route")&"' AND plate_no = '"&RowData.Get("Truck")&"'")
	If cursor6.RowCount > 0 Then
		For ia = 0 To cursor6.RowCount - 1
			Sleep(10)
			cursor6.Position = ia
			return_route_id = cursor6.GetString("return_route_id")
			LABEL_LOAD_ROUTE_NAME.Text = cursor6.GetString("route_name")
			LABEL_LOAD_PLATE.Text = cursor6.GetString("plate_no")
			LABEL_LOAD_DRIVER.Text = cursor6.GetString("driver")
			LABEL_LOAD_HELPER.Text = cursor6.GetString("helper1")
			LABEL_LOAD_HELPER2.Text = cursor6.GetString("helper2")
			LABEL_LOAD_HELPER3.Text = cursor6.GetString("helper3")
			LVL_PICKLIST.AddTwoLines2(cursor6.GetString("picklist_name"),cursor6.GetString("picklist_id"),cursor6.GetString("picklist_id") & "|" & cursor6.GetString("picklist_name"))
			LABEL_LOAD_DATE.Text = cursor6.GetString("date_return")
			LABEL_LOAD_TIME.Text = cursor6.GetString("time_return")
		Next
		SHOW_SAVED_ROUTE
	Else
		ToastMessageShow("Invoice is empty", False)
	End If

End Sub
Sub TABLE_ROUTE_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array (NameColumn(0),NameColumn(1),NameColumn(2),NameColumn(3),NameColumn(4))

		Dim MaxWidth As Int
'		Dim MaxHeight As Int
		For i = 0 To TABLE_ROUTE.VisibleRowIds.Size
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
	If ShouldRefresh Then
		TABLE_ROUTE.Refresh
		XSelections.Clear
	End If
End Sub

Sub SHOW_ROUTE
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LVL_PICKLIST.Background = bg
	Sleep(0)
	LVL_PICKLIST.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LVL_PICKLIST.TwoLinesLayout.Label.TextSize = 16
	LVL_PICKLIST.TwoLinesLayout.label.Height = 8%y
	LVL_PICKLIST.TwoLinesLayout.label.Top = -0.5%y
	LVL_PICKLIST.TwoLinesLayout.Label.TextColor = Colors.Black
	LVL_PICKLIST.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Top = 5%y
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextSize = 13
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Height = 4%y
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LVL_PICKLIST.TwoLinesLayout.ItemHeight = 8%y
	
	Dim bg2 As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg2.Initialize2(Colors.ARGB(255,255,0,242), 5, 0, Colors.LightGray)
	BUTTON_DELIVERED.Background = bg2
	BUTTON_DELIVERED.Text = "DELIVERED"
	
	CTRL.HideKeyboard
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
	Sleep(0)
	PANEL_BG_ROUTE.SetVisibleAnimated(300, True)
	Sleep(100)
	LABEL_LOAD_DATE.Enabled = True
	LABEL_LOAD_TIME.Enabled = True
	OpenLabel(LABEL_LOAD_DATE)
	
	transaction_type = "NEW"
End Sub
Sub SHOW_SAVED_ROUTE
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LVL_PICKLIST.Background = bg
	Sleep(0)
	LVL_PICKLIST.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LVL_PICKLIST.TwoLinesLayout.Label.TextSize = 16
	LVL_PICKLIST.TwoLinesLayout.label.Height = 8%y
	LVL_PICKLIST.TwoLinesLayout.label.Top = -0.5%y
	LVL_PICKLIST.TwoLinesLayout.Label.TextColor = Colors.Black
	LVL_PICKLIST.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Top = 5%y
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextSize = 13
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Height = 4%y
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LVL_PICKLIST.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LVL_PICKLIST.TwoLinesLayout.ItemHeight = 8%y
	
	CTRL.HideKeyboard
	PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
	Sleep(0)
	PANEL_BG_ROUTE.SetVisibleAnimated(300, True)
	
	Dim bg2 As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg2.Initialize2(Colors.ARGB(255,0,173,255), 5, 0, Colors.LightGray)
	BUTTON_DELIVERED.Background = bg2
	BUTTON_DELIVERED.Text = "EDIT"
	
	LABEL_LOAD_DATE.Enabled = False
	LABEL_LOAD_TIME.Enabled = False
	
	transaction_type = "SAVED"
End Sub

Sub BUTTON_CANCELLED_Click
	If transaction_type = "SAVED" Then
		date_return = LABEL_LOAD_DATE.Text
		route_name = LABEL_LOAD_ROUTE_NAME.Text
		plate_no = LABEL_LOAD_PLATE.Text
		PANEL_BG_ROUTE.SetVisibleAnimated(300,False)
		Sleep(0)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Sleep(500)
		StartActivity(CANCELLED_MODULE)
		SetAnimation("right_to_center", "center_to_left")
	Else
		If LABEL_LOAD_DATE.Text = "" Then
			ToastMessageShow("Please input a date returned.", False)
		Else If LABEL_LOAD_TIME.Text = "" Then
			ToastMessageShow("Please input a time returned", False)
		Else
			Dim picklist_details As String
			Msgbox2Async("After you mark this as delivered, we will proceed you to cancelled picklst. Are you sure you mark as delivered this route?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				NEW_TRANSACTION
				Sleep(0)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
				PANEL_BG_MSGBOX.BringToFront
				LABEL_HEADER_TEXT.Text = "Uploading Picklist"
				LABEL_MSGBOX2.Text = "Finalizing data..."
				For i = 0 To LVL_PICKLIST.Size - 1
					Sleep(100)
					picklist_details = LVL_PICKLIST.GetItem(i)
					picklist_id = picklist_details.SubString2(0,picklist_details.IndexOf("|"))
					Log(picklist_id)
					connection.ExecNonQuery("DELETE FROM picklist_return_ref_table WHERE picklist_id = '"&picklist_id&"'")
					Dim QUERY As String = "insert into picklist_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
					connection.ExecNonQuery2(QUERY,Array As String(return_route_id,picklist_details.SubString2(0,picklist_details.IndexOf("|")),picklist_details.SubString(picklist_details.IndexOf("|")+1),LABEL_LOAD_ROUTE_NAME.Text,LABEL_LOAD_PLATE.Text _
					,LABEL_LOAD_DRIVER.Text,LABEL_LOAD_HELPER.Text,LABEL_LOAD_HELPER2.Text,LABEL_LOAD_HELPER3.Text,LABEL_LOAD_DATE.Text,LABEL_LOAD_TIME.Text,DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now),LOGIN_MODULE.username,LOGIN_MODULE.tab_id))
					ToastMessageShow("Added Successfully",False)
					Sleep(0)
					Dim cmd As DBCommand = CreateCommand("update_return", Array As String(LABEL_LOAD_DATE.Text,LABEL_LOAD_TIME.Text,return_route_id ,LOGIN_MODULE.username,LOGIN_MODULE.tab_id, picklist_id))
					Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
					Wait For(js) JobDone(js As HttpJob)
					If js.Success Then
						UPDATE_DELIVERED
					Else
						PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
						Log("ERROR: " & js.ErrorMessage)
						Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						ToastMessageShow("Updating Error", False)
					End If
					js.Release
				Next
				LOAD_ROUTE
				Sleep(100)
				route_name = LABEL_LOAD_ROUTE_NAME.Text
				plate_no = LABEL_LOAD_PLATE.Text
				date_return = LABEL_LOAD_DATE.Text
				PANEL_BG_ROUTE.SetVisibleAnimated(300,False)
				Sleep(0)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				ToastMessageShow("Route mark as delivered.", False)
				Sleep(500)
				StartActivity(CANCELLED_MODULE)
				SetAnimation("right_to_center", "center_to_left")
			End If
		End If
	End If
End Sub
Sub BUTTON_RETURNS_Click
	If transaction_type = "SAVED" Then
		date_return = LABEL_LOAD_DATE.Text
		route_name = LABEL_LOAD_ROUTE_NAME.Text
		plate_no = LABEL_LOAD_PLATE.Text
		PANEL_BG_ROUTE.SetVisibleAnimated(300,False)
		Sleep(0)
		PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
		Sleep(500)
		StartActivity(SALES_RETURN_MODULE)
		SetAnimation("right_to_center", "center_to_left")
	Else
		If LABEL_LOAD_DATE.Text = "" Then
			ToastMessageShow("Please input a date returned.", False)
		Else If LABEL_LOAD_TIME.Text = "" Then
			ToastMessageShow("Please input a time returned", False)
		Else
			Dim picklist_details As String
			Msgbox2Async("After you mark this as delivered, we will proceed you to sales return of this picklist. Are you sure you mark as delivered this route?", "Warning", "YES", "", "NO", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
			Wait For Msgbox_Result (Result As Int)
			If Result = DialogResponse.POSITIVE Then
				NEW_TRANSACTION
				Sleep(0)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, True)
				PANEL_BG_MSGBOX.BringToFront
				LABEL_HEADER_TEXT.Text = "Uploading Picklist"
				LABEL_MSGBOX2.Text = "Finalizing data..."
				For i = 0 To LVL_PICKLIST.Size - 1
					Sleep(100)
					picklist_details = LVL_PICKLIST.GetItem(i)
					picklist_id = picklist_details.SubString2(0,picklist_details.IndexOf("|"))
					Log(picklist_id)
					connection.ExecNonQuery("DELETE FROM picklist_return_ref_table WHERE picklist_id = '"&picklist_id&"'")
					Dim QUERY As String = "insert into picklist_return_ref_table VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
					connection.ExecNonQuery2(QUERY,Array As String(return_route_id,picklist_details.SubString2(0,picklist_details.IndexOf("|")),picklist_details.SubString(picklist_details.IndexOf("|")+1),LABEL_LOAD_ROUTE_NAME.Text,LABEL_LOAD_PLATE.Text _
					,LABEL_LOAD_DRIVER.Text,LABEL_LOAD_HELPER.Text,LABEL_LOAD_HELPER2.Text,LABEL_LOAD_HELPER3.Text,LABEL_LOAD_DATE.Text,LABEL_LOAD_TIME.Text,DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now),LOGIN_MODULE.username,LOGIN_MODULE.tab_id))
					ToastMessageShow("Added Successfully",False)
					Sleep(0)
					Dim cmd As DBCommand = CreateCommand("update_return", Array As String(LABEL_LOAD_DATE.Text,LABEL_LOAD_TIME.Text,return_route_id ,LOGIN_MODULE.username,LOGIN_MODULE.tab_id, picklist_id))
					Dim js As HttpJob = CreateRequest.ExecuteBatch(Array(cmd), Null)
					Wait For(js) JobDone(js As HttpJob)
					If js.Success Then
						UPDATE_DELIVERED
					Else
						PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
						Log("ERROR: " & js.ErrorMessage)
						Msgbox2Async("SYSTEM NETWORK CAN'T CONNECT TO SERVER." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
						ToastMessageShow("Updating Error", False)
					End If
					js.Release
				Next
				LOAD_ROUTE
				Sleep(100)
				date_return = LABEL_LOAD_DATE.Text
				route_name = LABEL_LOAD_ROUTE_NAME.Text
				plate_no = LABEL_LOAD_PLATE.Text
				PANEL_BG_ROUTE.SetVisibleAnimated(300,False)
				Sleep(0)
				ToastMessageShow("Route mark as delivered.", False)
				PANEL_BG_MSGBOX.SetVisibleAnimated(300, False)
				Sleep(500)
				StartActivity(SALES_RETURN_MODULE)
				SetAnimation("right_to_center", "center_to_left")	
			End If
		End If
	End If
End Sub