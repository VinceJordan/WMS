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
	
	Public group_id As String
	
	Dim group_id_new As String
	
	Public clear_trigger As String
	
	Private cartBitmap As Bitmap
	Private downBitmap As Bitmap

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim ToolbarHelper As ACActionBar
	Private ACToolBarLight1 As ACToolBarLight
	
	Private Dialog As B4XDialog
	Private Base As B4XView
	Private SearchTemplate As B4XSearchTemplate
	Private SearchTemplate2 As B4XSearchTemplate
	
	Private XSelections As B4XTableSelections
	Private NameColumn(3) As B4XTableColumn
	
	Private cvs As B4XCanvas
	Private xui As XUI

	Private LABEL_LOAD_GROUP As Label
	Private CMB_CREATE_YEAR As B4XComboBox
	Private CMB_CREATE_MONTH As B4XComboBox
	Private PANEL_BG_CREATE As Panel
	Private PANEL_CREATE As Panel
	Private CMB_GROUP As B4XComboBox
	Private CMB_YEAR As B4XComboBox
	Private CMB_MONTH As B4XComboBox
	Private BUTTON_SAVE As Button
	Private BUTTON_DAILY_COUNT As Button
	Private TABLE_INVENTORY_DATE As B4XTable
	Private BUTTON_VIEW As Button
	Private BUTTON_EDIT As Button
	Private CMB_TABID As B4XComboBox
	Private PANEL_BG_DOWNLOAD As Panel
	Private LISTVIEW_TEMPLATES As ListView
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
	Activity.LoadLayout("daily")

	cartBitmap = LoadBitmap(File.DirAssets, "add.png")
	downBitmap = LoadBitmap(File.DirAssets, "download.png")
	
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

	Base = Activity
	Dialog.VisibleAnimationDuration = 300
	Dialog.Initialize(Base)
	Dialog.BorderColor = Colors.Transparent
	Dialog.BorderCornersRadius = 5
	Dialog.TitleBarColor = Colors.RGB(82,169,255)
	Dialog.TitleBarTextColor = Colors.White
	Dialog.BackgroundColor = Colors.White
	Dialog.ButtonsColor = Colors.White
	Dialog.ButtonsTextColor = Colors.Black
	Dialog.PutAtTop = True
	
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
	
	Dim p As B4XView = xui.CreatePanel("")
	p.SetLayoutAnimated(0, 0, 0, 1dip, 1dip)
	cvs.Initialize(p)

	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"

	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(0,142,255), 5, 0, Colors.LightGray)
	BUTTON_VIEW.Background = bg
	BUTTON_VIEW.Text = " View"
End Sub
Sub Activity_CreateMenu(Menu As ACMenu)
	Dim item As ACMenuItem = ACToolBarLight1.Menu.Add2(0, 0, "Create New Template", Null)
	item.ShowAsAction = item.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Create New Template", cartBitmap)
	
	Dim item1 As ACMenuItem = ACToolBarLight1.Menu.Add2(1, 1, "Download Template", Null)
	item1.ShowAsAction = item1.SHOW_AS_ACTION_ALWAYS
	UpdateIcon("Download Template", downBitmap)
End Sub

Sub Activity_Resume
	If clear_trigger = 1 Then
		LOAD_GROUP
		Sleep(10)
		CMB_GROUP.SelectedIndex = -1
		Sleep(10)
		OpenSpinner(CMB_GROUP.cmbBox)
		ENABLE_VIEW
	Else
		LOAD_INVENTORY_DATE
	End If
End Sub

Sub Activity_Pause (UserClosed As Boolean)

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
	StartActivity(DASHBOARD_MODULE)
	SetAnimation("left_to_center", "center_to_right")
	clear_trigger = 1
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
	If Item.Title = "Create New Template" Then
		ENABLE_VIEW
		Sleep(0)
		PANEL_BG_CREATE.BringToFront
		PANEL_BG_CREATE.SetVisibleAnimated(300, True)
		Sleep(100)
		OpenLabel(LABEL_LOAD_GROUP)
		Sleep(0)
		LOAD_CREATE_YEAR
		Sleep(0)
		LOAD_CREATE_MONTH
	Else If Item.Title = "Download Template" Then
		PANEL_BG_DOWNLOAD.SetVisibleAnimated(300,True)
		PANEL_BG_DOWNLOAD.BringToFront
		Sleep(0)
		GET_TABID
	Else
		
	End If
End Sub

Sub LOAD_GROUP
	CMB_GROUP.cmbBox.Clear
	CMB_YEAR.cmbBox.Clear
	CMB_MONTH.cmbBox.Clear
	cursor1 = connection.ExecQuery("SELECT group_name FROM daily_template_table GROUP BY group_name ORDER BY group_name ASC")
	If cursor1.RowCount > 0 Then
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			CMB_GROUP.cmbBox.Add(cursor1.GetString("group_name"))
		Next
	Else

	End If
End Sub
Sub CMB_GROUP_SelectedIndexChanged (Index As Int)
	LOAD_YEAR
	Sleep(10)
	CMB_YEAR.SelectedIndex = -1
	Sleep(10)
	OpenSpinner(CMB_YEAR.cmbBox)
End Sub
Sub LOAD_YEAR
	CMB_YEAR.cmbBox.Clear
	CMB_MONTH.cmbBox.Clear
	cursor2 = connection.ExecQuery("SELECT year FROM daily_template_table WHERE group_name = '"&CMB_GROUP.cmbBox.SelectedItem&"' GROUP BY year ORDER BY year ASC")
	If cursor2.RowCount > 0 Then
		For i = 0 To cursor2.RowCount - 1
			Sleep(0)
			cursor2.Position = i
			CMB_YEAR.cmbBox.Add(cursor2.GetString("year"))
		Next
	Else

	End If
End Sub
Sub CMB_YEAR_SelectedIndexChanged (Index As Int)
	LOAD_MONTH
	Sleep(10)
	CMB_MONTH.SelectedIndex = -1
	Sleep(10)
	OpenSpinner(CMB_MONTH.cmbBox)
End Sub
Sub LOAD_MONTH
	CMB_MONTH.cmbBox.Clear
	cursor3 = connection.ExecQuery("SELECT group_id, month FROM daily_template_table WHERE group_name = '"&CMB_GROUP.cmbBox.SelectedItem&"' and year = '"&CMB_YEAR.cmbBox.SelectedItem&"' ORDER BY group_id ASC")
	If cursor3.RowCount > 0 Then
		For i = 0 To cursor3.RowCount - 1
			Sleep(0)
			cursor3.Position = i
			CMB_MONTH.cmbBox.Add(cursor3.GetString("month"))
		Next
	Else

	End If
End Sub

Sub BUTTON_CREATE_Click
	PANEL_BG_CREATE.BringToFront
	PANEL_BG_CREATE.SetVisibleAnimated(300, True)
	Sleep(100)
	OpenLabel(LABEL_LOAD_GROUP)
	Sleep(0)
	LOAD_CREATE_YEAR
	Sleep(0)
	LOAD_CREATE_MONTH
End Sub
Sub GROUP_SUGGESTION
	SearchTemplate.CustomListView1.Clear
	Dialog.Title = "Creating a Group Name"
	Dim Items As List
	Items.Initialize
	Items.Clear
	cursor1 = connection.ExecQuery("SELECT group_name FROM daily_template_table GROUP BY group_name ORDER BY group_name ASC")
	If cursor1.RowCount > 0 Then
		For i = 0 To cursor1.RowCount - 1
			Sleep(0)
			cursor1.Position = i
			Items.Add(cursor1.GetString("group_name"))
		Next
		SearchTemplate.SetItems(Items)
	Else
		Items.Add("")
		SearchTemplate.SetItems(Items)
	End If
End Sub
Sub LABEL_LOAD_GROUP_Click
	GROUP_SUGGESTION
	Sleep(100)
	Dim rs As ResumableSub = Dialog.ShowTemplate(SearchTemplate, "", "ENTER", "CANCEL")
	Dialog.Base.Top = 38%y - Dialog.Base.Height / 2
	Wait For (rs) Complete (Result As Int)
	If Result = xui.DialogResponse_Positive Then
		If SearchTemplate.SelectedItem = "" Then
			LABEL_LOAD_GROUP.Text = "Click me to  add new group."
		Else
			LABEL_LOAD_GROUP.Text = SearchTemplate.SelectedItem
		End If
		OpenSpinner(CMB_CREATE_YEAR.cmbBox)
		Sleep(0)
		CMB_CREATE_YEAR.SelectedIndex = - 1
	Else If Result = xui.DialogResponse_Negative Then
		If SearchTemplate.SearchField.Text = "" Then
			LABEL_LOAD_GROUP.Text = "Click me to  add new group."
			OpenSpinner(CMB_CREATE_YEAR.cmbBox)
			Sleep(0)
			CMB_CREATE_YEAR.SelectedIndex = - 1
		Else
			LABEL_LOAD_GROUP.Text = SearchTemplate.SearchField.Text.ToUpperCase
			OpenSpinner(CMB_CREATE_YEAR.cmbBox)
			Sleep(0)
			CMB_CREATE_YEAR.SelectedIndex = - 1
		End If
	Else
		LABEL_LOAD_GROUP.Text = "Click me to  add new group."
		OpenSpinner(CMB_CREATE_YEAR.cmbBox)
		Sleep(0)
		CMB_CREATE_YEAR.SelectedIndex = - 1
	End If
End Sub
Sub LOAD_CREATE_YEAR
	CMB_CREATE_YEAR.cmbBox.DropdownTextColor = Colors.Black
	CMB_CREATE_YEAR.cmbBox.TextColor = Colors.White
	CMB_CREATE_YEAR.cmbBox.Clear
	For i = 0 To 10
		CMB_CREATE_YEAR.cmbBox.Add(DateTime.GetYear(DateTime.now) + i)
	Next
End Sub
Sub CMB_CREATE_YEAR_SelectedIndexChanged (Index As Int)
	OpenSpinner(CMB_CREATE_MONTH.cmbBox)
	Sleep(0)
	CMB_CREATE_MONTH.SelectedIndex = - 1
End Sub
Sub LOAD_CREATE_MONTH
	CMB_CREATE_MONTH.cmbBox.DropdownTextColor = Colors.Black
	CMB_CREATE_MONTH.cmbBox.TextColor = Colors.White
	CMB_CREATE_MONTH.cmbBox.Clear
	CMB_CREATE_MONTH.cmbBox.Add("January")
	CMB_CREATE_MONTH.cmbBox.Add("February")
	CMB_CREATE_MONTH.cmbBox.Add("March")
	CMB_CREATE_MONTH.cmbBox.Add("April")
	CMB_CREATE_MONTH.cmbBox.Add("May")
	CMB_CREATE_MONTH.cmbBox.Add("June")
	CMB_CREATE_MONTH.cmbBox.Add("July")
	CMB_CREATE_MONTH.cmbBox.Add("August")
	CMB_CREATE_MONTH.cmbBox.Add("September")
	CMB_CREATE_MONTH.cmbBox.Add("October")
	CMB_CREATE_MONTH.cmbBox.Add("November")
	CMB_CREATE_MONTH.cmbBox.Add("December")
End Sub
Sub CMB_MONTH_SelectedIndexChanged (Index As Int)
	
End Sub
Sub CREATE_GROUP_ID
	Dim month_num As String
	
	If CMB_CREATE_MONTH.SelectedIndex = -1 Then CMB_CREATE_MONTH.SelectedIndex = 0
	
	If CMB_CREATE_YEAR.SelectedIndex = -1 Then CMB_CREATE_YEAR.SelectedIndex = 0
	
	If CMB_CREATE_MONTH.cmbBox.SelectedItem = "January" Then
		month_num = "01"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "February" Then
		month_num = "02"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "March" Then
		month_num = "03"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "April" Then
		month_num = "04"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "May" Then
		month_num = "05"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "June" Then
		month_num = "06"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "July" Then
		month_num = "07"
	ELse If CMB_CREATE_MONTH.cmbBox.SelectedItem = "August" Then
		month_num = "08"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "September" Then
		month_num = "09"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "October" Then
		month_num = "10"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "November" Then
		month_num = "11"
	Else If CMB_CREATE_MONTH.cmbBox.SelectedItem = "December" Then
		month_num = "12"
	End If
	
	group_id = LABEL_LOAD_GROUP.Text&CMB_CREATE_YEAR.cmbBox.SelectedItem&month_num&LOGIN_MODULE.tab_id
End Sub
Sub BUTTON_SAVE_Click
	If LABEL_LOAD_GROUP.Text.Length <= 2 Then
		Msgbox2Async("Group name must be 3 or more characters or letters. Cannot proceed.", "Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)	
	Else
	Msgbox2Async("Are you sure you want to create this template?", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		CREATE_GROUP_ID
		Sleep(0)
		Dim query As String = "SELECT * FROM daily_template_table WHERE group_name = ? and year = ? and month = ?"
		cursor3 = connection.ExecQuery2(query,Array As String(LABEL_LOAD_GROUP.Text,CMB_CREATE_YEAR.cmbBox.SelectedItem,CMB_CREATE_MONTH.cmbBox.SelectedItem))
		If cursor3.RowCount > 0 Then
				Msgbox2Async("The template you creating is already exisiting in the system. Cannot proceed.", "Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
		Else
			Dim query As String = "INSERT INTO daily_template_table VALUES (?,?,?,?,?,?,?,?,?,?,?)"
			connection.ExecNonQuery2(query,Array As String(group_id,LABEL_LOAD_GROUP.Text,CMB_CREATE_MONTH.cmbBox.SelectedItem,CMB_CREATE_YEAR.cmbBox.SelectedItem,DateTime.Date(DateTime.Now),DateTime.Time(DateTime.Now),0,"-","-",LOGIN_MODULE.username,LOGIN_MODULE.tab_id))
			ToastMessageShow("Template created", False)
			Sleep(0)
			PANEL_BG_CREATE.SetVisibleAnimated(300, False)
			Sleep(0)
			StartActivity(DAILY_TEMPLATE)
			SetAnimation("right_to_center", "center_to_left")
			clear_trigger = 1
		End If
	Else
		
	End If
	End If
End Sub
Sub BUTTON_CANCEL_Click
	PANEL_BG_CREATE.SetVisibleAnimated(300, False)
	LABEL_LOAD_GROUP.Text = "Click me to  add new group."
End Sub

Sub BUTTON_EDIT_Click
	If CMB_YEAR.cmbBox.SelectedIndex = CMB_YEAR.cmbBox.IndexOf("") Or CMB_MONTH.cmbBox.SelectedIndex = CMB_MONTH.cmbBox.IndexOf("") Then
		Msgbox2Async("Please choose year and month. Cannot proceed.", "Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
	Else
		If CMB_GROUP.SelectedIndex = -1 Then CMB_GROUP.SelectedIndex = 0
		If CMB_YEAR.SelectedIndex = -1 Then CMB_YEAR.SelectedIndex = 0
		If CMB_MONTH.SelectedIndex = -1 Then CMB_MONTH.SelectedIndex = 0
		
		Dim query As String = "SELECT * FROM daily_template_table WHERE group_name = ? and year = ? and month = ?"
		cursor5 = connection.ExecQuery2(query,Array As String(CMB_GROUP.cmbBox.SelectedItem,CMB_YEAR.cmbBox.SelectedItem,CMB_MONTH.cmbBox.SelectedItem))
		If cursor5.RowCount > 0 Then
			For i = 0 To cursor5.RowCount - 1
				cursor5.Position = i
				group_id = cursor5.GetString("group_id")
			Next
			Sleep(0)
			StartActivity(DAILY_TEMPLATE)
			SetAnimation("right_to_center", "center_to_left")
			clear_trigger = 0
		Else
		
		End If
	End If
End Sub

Sub ENABLE_VIEW
	BUTTON_EDIT.Visible = False
	BUTTON_DAILY_COUNT.Visible = False
	CMB_GROUP.cmbBox.Enabled = True
	CMB_YEAR.cmbBox.Enabled = True
	CMB_MONTH.cmbBox.Enabled = True
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.RGB(0,142,255), 5, 0, Colors.LightGray)
	BUTTON_VIEW.Background = bg
	BUTTON_VIEW.Text = " View"
End Sub

Sub DISABLE_VIEW
	BUTTON_DAILY_COUNT.Visible = True
	BUTTON_EDIT.Visible = True
	Sleep(0)
	CMB_GROUP.cmbBox.Enabled = False
	CMB_YEAR.cmbBox.Enabled = False
	CMB_MONTH.cmbBox.Enabled = False
	Dim bg As ColorDrawable
	bg.Initialize2(Colors.Red, 5, 0, Colors.LightGray)
	BUTTON_VIEW.Background = bg
	BUTTON_VIEW.Text = " Close"
End Sub

Sub BUTTON_VIEW_Click
	If BUTTON_VIEW.Text = " View" Then
		If CMB_YEAR.cmbBox.SelectedIndex = CMB_YEAR.cmbBox.IndexOf("") Or CMB_MONTH.cmbBox.SelectedIndex = CMB_MONTH.cmbBox.IndexOf("") Then
			Msgbox2Async("Please choose year and month. Cannot proceed.", "Warning", "OK", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), False)
		Else
			If CMB_GROUP.SelectedIndex = -1 Then CMB_GROUP.SelectedIndex = 0
			If CMB_YEAR.SelectedIndex = -1 Then CMB_YEAR.SelectedIndex = 0
			If CMB_MONTH.SelectedIndex = -1 Then CMB_MONTH.SelectedIndex = 0
		
			Dim query As String = "SELECT * FROM daily_template_table WHERE group_name = ? and year = ? and month = ?"
			cursor5 = connection.ExecQuery2(query,Array As String(CMB_GROUP.cmbBox.SelectedItem,CMB_YEAR.cmbBox.SelectedItem,CMB_MONTH.cmbBox.SelectedItem))
			If cursor5.RowCount > 0 Then
				For i = 0 To cursor5.RowCount - 1
					cursor5.Position = i
					group_id = cursor5.GetString("group_id")
				Next
				LOAD_TABLE_HEADER
				Sleep(0)
				LOAD_INVENTORY_DATE
				Sleep(0)
				DISABLE_VIEW
				
			Else

			End If
		End If
	Else
		TABLE_INVENTORY_DATE.Clear
		Sleep(0)
		ENABLE_VIEW
	End If
	
End Sub

Sub BUTTON_DAILY_COUNT_Click
	StartActivity(DAILY_COUNT)
	DAILY_COUNT.inventory_date = DateTime.Date(DateTime.now)
	clear_trigger = 0
End Sub

Sub LOAD_TABLE_HEADER
	NameColumn(0)=TABLE_INVENTORY_DATE.AddColumn("Inventory Date", TABLE_INVENTORY_DATE.COLUMN_TYPE_TEXT)
	NameColumn(1)=TABLE_INVENTORY_DATE.AddColumn("User", TABLE_INVENTORY_DATE.COLUMN_TYPE_TEXT)
	NameColumn(2)=TABLE_INVENTORY_DATE.AddColumn("Status", TABLE_INVENTORY_DATE.COLUMN_TYPE_TEXT)
End Sub
Sub LOAD_INVENTORY_DATE
	Sleep(0)
	Dim Data As List
	Data.Initialize
	cursor4 = connection.ExecQuery("SELECT * FROM daily_inventory_disc_table WHERE group_id = '"&group_id&"' GROUP BY inventory_date ORDER by inventory_date DESC")
	If cursor4.RowCount > 0 Then
		For ia = 0 To cursor4.RowCount - 1
			Sleep(0)
			cursor4.Position = ia
			Dim row(3) As Object
			row(0) = cursor4.GetString("inventory_date")
			row(1) = cursor4.GetString("user_info")
			row(2) = cursor4.GetString("status")
			Data.Add(row)
		Next
	Else
		ToastMessageShow("No exisiting inventory date", False)
	End If
	TABLE_INVENTORY_DATE.SetData(Data)
	If XSelections.IsInitialized = False Then
		XSelections.Initialize(TABLE_INVENTORY_DATE)
		XSelections.Mode = XSelections.MODE_SINGLE_LINE_PERMANENT
	End If
	Sleep(0)
End Sub
Sub TABLE_INVENTORY_DATE_CellClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)
End Sub
Sub TABLE_INVENTORY_DATE_CellLongClicked (ColumnId As String, RowId As Long)
	XSelections.CellClicked(ColumnId, RowId)	
	
	Dim RowData As Map = TABLE_INVENTORY_DATE.GetRow(RowId)
	Dim cell As String = RowData.Get(ColumnId)

	Log(RowData.Get("Invetory Date"))

	Msgbox2Async("Inventory Date : " & RowData.Get("Inventory Date") & CRLF _
	& "Status : " & RowData.Get("Status") & CRLF _
	, "Option", "Proceed", "", "", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		StartActivity(DAILY_COUNT)
		DAILY_COUNT.inventory_date = RowData.Get("Inventory Date")
		clear_trigger = 0
	Else if Result = DialogResponse.NEGATIVE Then
	
	Else if Result = DialogResponse.CANCEL Then
		TABLE_INVENTORY_DATE.Refresh
	End If
End Sub
Sub TABLE_INVENTORY_DATE_DataUpdated
	Dim ShouldRefresh As Boolean
	For Each Column As B4XTableColumn In Array ()
'		NameColumn(0), NameColumn(1), NameColumn(2)
		Dim MaxWidth As Int
		For i = 0 To TABLE_INVENTORY_DATE.VisibleRowIds.Size
			Dim pnl As B4XView = Column.CellsLayouts.Get(i)
			Dim lbl As B4XView = pnl.GetView(0)
			MaxWidth = Max(MaxWidth, cvs.MeasureText(lbl.Text, lbl.Font).Width + 10dip)
		Next
		If MaxWidth > Column.ComputedWidth Or MaxWidth < Column.ComputedWidth - 20dip Then
			Column.Width = MaxWidth + 10dip
			ShouldRefresh = True
		End If
	Next
	For i = 0 To TABLE_INVENTORY_DATE.VisibleRowIds.Size - 1
		Dim RowId As Long = TABLE_INVENTORY_DATE.VisibleRowIds.Get(i)
		If RowId > 0 Then
			Dim pnl1 As B4XView = NameColumn(2).CellsLayouts.Get(i + 1) '+1 because the first cell is the header
			Dim row As Map = TABLE_INVENTORY_DATE.GetRow(RowId)
			Dim clr As Int
			Dim OtherColumnValue As String = row.Get(NameColumn(2).Id)
			Log(row.Get(NameColumn(2).Id))
			If OtherColumnValue = ("SAVED") Then
				clr = xui.Color_Red
			Else If OtherColumnValue = ("FINAL") Then
				clr = xui.Color_Green
			Else
				clr = xui.Color_ARGB(255,82,169,255)
			End If
			pnl1.GetView(0).Color = clr
			
		End If
	Next
	If ShouldRefresh Then
		TABLE_INVENTORY_DATE.Refresh
		XSelections.Clear
		XSelections.Refresh
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
Sub GET_TABID
	ProgressDialogShow2("Loading...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_tab_id", Array(LOGIN_MODULE.tab_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		ProgressDialogHide
		CMB_TABID.cmbBox.Clear
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
		For Each row() As Object In res.Rows
				CMB_TABID.cmbBox.Add("TABLET " & row(res.Columns.Get("tab_id")))
			Sleep(0)
		Next
		Else
			CMB_TABID.cmbBox.Clear
		End If
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("TAB ID IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub GET_TEMPLATES
	LIST_TEMPLATE
	ProgressDialogShow2("Loading...", False)
	Dim req As DBRequestManager = CreateRequest
	Log(CMB_TABID.cmbBox.SelectedItem.SubString2(7,8))
	Dim cmd As DBCommand = CreateCommand("select_tab_template", Array(CMB_TABID.cmbBox.SelectedItem.SubString2(7,8)))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		ProgressDialogHide
		LISTVIEW_TEMPLATES.Clear
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
		For Each row() As Object In res.Rows
				LISTVIEW_TEMPLATES.AddTwoLines2(row(res.Columns.Get("month")) & " " & row(res.Columns.Get("year")),row(res.Columns.Get("group_name")),row(res.Columns.Get("group_id")))
			Sleep(0)
		Next
		Else
			LISTVIEW_TEMPLATES.Clear
		End If
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("CAN'T CONNECT TO SYSTEM." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
Sub GET_DAILY_TEMPLATE
	ProgressDialogShow2("Loading...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_tab_daily_template", Array(group_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		ProgressDialogHide
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
				Log(row(res.Columns.Get("group_name")))
				Log(row(res.Columns.Get("year")))
				Log(row(res.Columns.Get("month")))
				cursor6 = connection.ExecQuery("SELECT * FROM daily_template_table WHERE group_name = '"&row(res.Columns.Get("group_name"))&"' AND year = '"&row(res.Columns.Get("year"))&"' AND month = '"&row(res.Columns.Get("month"))&"'")
				If cursor6.RowCount > 0 Then
					For i = 0 To cursor6.RowCount - 1
						Sleep(0)
						cursor6.Position = i
						Msgbox2Async("This template is already existing in your tablet. Please check your template name you searching.", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					Next
					
				Else
					Dim month_num As String
					
					If row(res.Columns.Get("month")) = "January" Then
						month_num = "01"
					Else If row(res.Columns.Get("month")) = "February" Then
						month_num = "02"
					Else If row(res.Columns.Get("month")) = "March" Then
						month_num = "03"
					Else If row(res.Columns.Get("month")) = "April" Then
						month_num = "04"
					Else If row(res.Columns.Get("month")) = "May" Then
						month_num = "05"
					Else If row(res.Columns.Get("month")) = "June" Then
						month_num = "06"
					Else If row(res.Columns.Get("month")) = "July" Then
						month_num = "07"
					ELse If row(res.Columns.Get("month")) = "August" Then
						month_num = "08"
					Else If row(res.Columns.Get("month")) = "September" Then
						month_num = "09"
					Else If row(res.Columns.Get("month")) = "October" Then
						month_num = "10"
					Else If row(res.Columns.Get("month")) = "November" Then
						month_num = "11"
					Else If row(res.Columns.Get("month")) = "December" Then
						month_num = "12"
					End If
					
					
					group_id_new = row(res.Columns.Get("group_name"))&row(res.Columns.Get("year"))&month_num&LOGIN_MODULE.tab_id
'					ToastMessageShow("added", False)
					connection.ExecNonQuery("INSERT INTO daily_template_table VALUES ('"&group_id_new&"','"&row(res.Columns.Get("group_name"))& _
					"','"&row(res.Columns.Get("month"))&"','"&row(res.Columns.Get("year"))&"','"&DateTime.Date(DateTime.Now)&"','"&DateTime.Time(DateTime.Now)& _
					"','0','-','-','"&LOGIN_MODULE.username&"','"&LOGIN_MODULE.tab_id&"')")
					Sleep(00)
					jr.Release
					Sleep(10)
					GET_DAILY_TEMPLATE_REF
				End If
			Next
		Else

		End If
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("CAN'T CONNECT TO SYSTEM." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
		jr.Release
	End If
End Sub
Sub GET_DAILY_TEMPLATE_REF
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_tab_template_ref", Array(group_id))
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		If res.Rows.Size > 0 Then
			For Each row() As Object In res.Rows
'					ToastMessageShow("added", False)
					connection.ExecNonQuery("INSERT INTO daily_inventory_ref_table VALUES ('"&group_id_new&"','"&row(res.Columns.Get("principal_id"))& _
					"','"&row(res.Columns.Get("principal_name"))&"','"&row(res.Columns.Get("product_id"))&"','"&row(res.Columns.Get("product_variant"))& _
					"','"&row(res.Columns.Get("product_description"))&"','"&DateTime.Date(DateTime.Now)&"','"&DateTime.Time(DateTime.Now)&"','"&LOGIN_MODULE.tab_id&"','"&LOGIN_MODULE.username&"')")
			Next
			PANEL_BG_DOWNLOAD.SetVisibleAnimated(300,False)
			ToastMessageShow("Template Downloaded Succesfully", False)
			LOAD_GROUP
			Sleep(10)
			CMB_GROUP.SelectedIndex = -1
			Sleep(10)
			OpenSpinner(CMB_GROUP.cmbBox)
			ENABLE_VIEW
			LISTVIEW_TEMPLATES.Clear
		Else

		End If
	Else
		ProgressDialogShow2("Error in Updating.", False)
		Sleep(1000)
		ProgressDialogHide
		Msgbox2Async("CAN'T CONNECT TO SYSTEM." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
		ToastMessageShow("Updating Error", False)
	End If
	jr.Release
End Sub
#End Region

Sub LIST_TEMPLATE
	Dim bg As ColorDrawable
'	bg.Initialize2(Colors.ARGB(130,97,97,97), 5, 0, Colors.Black)
	bg.Initialize2(Colors.White, 5, 1, Colors.LightGray)
	LISTVIEW_TEMPLATES.Background = bg
	LISTVIEW_TEMPLATES.Clear
	Sleep(0)
	LISTVIEW_TEMPLATES.TwoLinesLayout.Label.Typeface = Typeface.DEFAULT
	LISTVIEW_TEMPLATES.TwoLinesLayout.Label.TextSize = 20
	LISTVIEW_TEMPLATES.TwoLinesLayout.label.Top = 0.5%y
	LISTVIEW_TEMPLATES.TwoLinesLayout.label.Height = 5%y
	LISTVIEW_TEMPLATES.TwoLinesLayout.Label.TextColor = Colors.Black
	LISTVIEW_TEMPLATES.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Typeface = Typeface.DEFAULT
	LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Top = 4.5%y
	LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.TextSize = 14
	LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Height = 3%y
	LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.TextColor = Colors.Gray
	LISTVIEW_TEMPLATES.TwoLinesLayout.SecondLabel.Gravity = Gravity.TOP
	LISTVIEW_TEMPLATES.TwoLinesLayout.ItemHeight = 8%y
End Sub

Sub BUTTON_DOWNLOAD_EXIT_Click
	PANEL_BG_DOWNLOAD.SetVisibleAnimated(300,False)
	LISTVIEW_TEMPLATES.Clear
End Sub

Sub BUTTON_FIND_Click
	GET_TEMPLATES
End Sub

Sub LISTVIEW_TEMPLATES_ItemClick (Position As Int, Value As Object)
	Msgbox2Async("Do you want to download this template?", "Warning", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		group_id = Value
		Sleep(0)
		GET_DAILY_TEMPLATE
			
	End If
End Sub