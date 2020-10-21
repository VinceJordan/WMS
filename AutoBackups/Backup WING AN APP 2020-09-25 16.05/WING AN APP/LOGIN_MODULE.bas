B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=8
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	'Permission
	Private rp As RuntimePermissions
	
	'Cursor
	Dim cursor1 As Cursor
	Dim cursor2 As Cursor
	Dim cursor3 As Cursor
	
	'PhoneId
	Dim phone_id As PhoneId
	
	'SQL
	Dim connection As SQL
	
	'String
	Public tab_id As String
	Public username As String
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	'IME
	Dim ctrl As IME
	
	'Carousel
	Private cv1 As CarouselView
	Dim bm As Bitmap
	Dim blist As List
	
	Private EDITTEXT_PASS As EditText
	Private SPINNER_USER As Spinner
	Private LABEL_LOAD_LASTLOG As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("login_design")
	
	If connection.IsInitialized = False Then
		connection.Initialize(File.DirRootExternal & "/WING AN APP/","tablet_db.db", False)
	End If
	
	Sleep(0)
	LOAD_USER
	
	Dim bg As ColorDrawable
	bg.Initialize(Colors.Transparent, 0)
	EDITTEXT_PASS.Background = bg
	SPINNER_USER.Background = bg
	
	Carousel
	
	EDITTEXT_PASS.Text = ""
	
	DateTime.TimeFormat = "hh:mm a"
	DateTime.DateFormat = "yyyy-MM-dd"
	
End Sub

Sub Activity_Resume
	EDITTEXT_PASS.Text = ""
		
	GET_LAST_LOG
	
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	SetAnimation("zoom_enter", "zoom_exit")
End Sub

Sub Carousel
	
	blist.Initialize                                     'initialize an empty list
	 
	bm.Initialize(File.DirAssets, "image_1.jpg")
	blist.Add(bm)                                        'add the image to the list
	bm.Initialize(File.DirAssets, "image_2.jpg")
	blist.Add(bm)
'	bm.Initialize(File.DirAssets, "image_3.jpg")
'	blist.Add(bm)
'	bm.Initialize(File.DirAssets, "image_4.jpg")
'	blist.Add(bm)
'	bm.Initialize(File.DirAssets, "image_5.jpg")
'	blist.Add(bm)
'	 
	cv1.ImageBitmaps = blist                              'pass the list with images to the library

	cv1.Radius = 5                                       'the radius of the indicator circles
	cv1.StrokeColor = Colors.Gray                      'the outline color of the circles
	cv1.StrokeWidth = 3                                   'the width of the outline circles
	cv1.FillColor = Colors.White                          'the color to fill the circles with - active page/image
	cv1.AutoPlay = True                                   'play automatically?
	cv1.CurrentItem = 0                                   'set the indeks of where to initially start from: 0 to (number of image - 1)
	cv1.DisableAutoPlayOnUserInteraction = True	       'stop auto play if image is touched during autoplay
	cv1.Orientation = cv1.ORIENTATION_HORIZONTAL          'can also be ORIENTATION_VERTICAL in which case it will be position to the left of the CarouselView
	cv1.PageCount = blist.Size                            'the number of pages/images to display
	cv1.PageColor = Colors.Transparent	                   'the color in the centre of the indicator circles for "not active" pages
	cv1.SlideInterval = 3000                              'the slide interval in milli seconds
	  
	cv1.playCarousel                                      'kickstart the carousel into action
	 
	' there is also cv1.pauseCarousel that can be used to pause the carousel
End Sub

#Region LOGIN
Sub LOAD_USER
	SPINNER_USER.Clear
	cursor1 = connection.ExecQuery("SELECT User FROM users_table ORDER BY User ASC")
	For i = 0 To cursor1.RowCount - 1
		Sleep(0)
		cursor1.Position = i
		SPINNER_USER.Add(cursor1.GetString("User"))
		SPINNER_USER.DropdownBackgroundColor = Colors.White
	Next
End Sub
Sub LOGGER
'	Activity.Finish
	GET_TOKEN
	Sleep(0)
	StartActivity(DASHBOARD_MODULE)
End Sub
Sub BUTTON_LOGIN_Click
	If EDITTEXT_PASS.Text = "" Then
		ToastMessageShow("Password is empty", False)
	Else
		cursor2 = connection.ExecQuery("SELECT * FROM users_table WHERE user = '"&SPINNER_USER.SelectedItem&"' and pass = '"&EDITTEXT_PASS.Text&"'")
		If cursor2.RowCount > 0 Then
			For i = 0 To cursor2.RowCount - 1
				rp.CheckAndRequest(rp.PERMISSION_READ_PHONE_STATE)
				Wait For Activity_PermissionResult (Permission As String, Result As Boolean)
				If Result Then
					ctrl.HideKeyboard
					Sleep(0)
					cursor2.Position = i
					If phone_id.GetDeviceId = "352948097840379" Then
						tab_id = "DEBUGGER"
						LOGGER
					else If phone_id.GetDeviceId = "863129042456148" Then
						tab_id = "A"
						LOGGER
					else If phone_id.GetDeviceId = "863129042438930" Then
						tab_id = "B"
						LOGGER
					else If phone_id.GetDeviceId = "863129042452949" Then
						tab_id = "C"
						LOGGER
					else If phone_id.GetDeviceId = "863129042466881" Then
						tab_id = "D"
						LOGGER
					else If phone_id.GetDeviceId = "359667094931063" Then
						tab_id = "S-D"
						LOGGER
					else If phone_id.GetDeviceId = "863129042467103" Then
						tab_id = "E"
						LOGGER
					else If phone_id.GetDeviceId = "359667094930842" Then
						tab_id = "S-E"
						LOGGER
					else If phone_id.GetDeviceId = "863129042658685" Then
						tab_id = "F"
						LOGGER
					else If phone_id.GetDeviceId = "359667094931014" Then
						tab_id = "S-F"
						LOGGER
					else If phone_id.GetDeviceId = "863129042464068" Then
						tab_id = "G"
						LOGGER
					else If phone_id.GetDeviceId = "863129042672041" Then
						tab_id = "H"
						LOGGER
					else If phone_id.GetDeviceId = "863129042450661" Then
						tab_id = "I"
						LOGGER
					else If phone_id.GetDeviceId = "356136101100354" Then
						tab_id = "S-M"
						LOGGER
					else If phone_id.GetDeviceId = "356136101101360" Then
						tab_id = "S-N"
						LOGGER
					else If phone_id.GetDeviceId = "356136101096925" Then
						tab_id = "S-O"
						LOGGER
					Else
						Msgbox2Async("Your device is not registered, Please inform your IT Department" , "Block", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
					End If
				Else
					Msgbox2Async("Please accept phone permission to proceed." , "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
				End If
			Next
			username = SPINNER_USER.SelectedItem 
		Else
			If EDITTEXT_PASS.Text <> "" Then
				ToastMessageShow("Password doesn't match", False)
				EDITTEXT_PASS.Text = ""
			End If
		End If
	End If
End Sub
Sub BUTTON_QUESTION_Click
	Msgbox2Async("Are you sure you want to update user account?" & CRLF & "Time span: 1-2 minutes", "Notice", "YES", "", "CANCEL", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	Wait For Msgbox_Result (Result As Int)
	If Result = DialogResponse.POSITIVE Then
		UPDATE_USER
	Else
		
	End If
	
End Sub
#End Region

Sub Activity_KeyPress (KeyCode As Int) As Boolean
	If KeyCode = KeyCodes.KEYCODE_BACK Then
		Return True
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
Sub UPDATE_USER
	ProgressDialogShow2("Updating accounts...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_user", Null)
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		connection.ExecNonQuery("DELETE FROM users_table")
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		For Each row() As Object In res.Rows
			Dim buffer2() As Byte
			buffer2 = row(res.Columns.Get("picture"))
			Dim InputStream1 As InputStream
			InputStream1.InitializeFromBytesArray(buffer2, 0, buffer2.Length)
			If row(res.Columns.Get("Department")) <> "SALES DEPARTMENT" Then
				connection.ExecNonQuery2("INSERT INTO users_table VALUES (?,?,?,?,?)", Array As Object(row(res.Columns.Get("User")),row(res.Columns.Get("Pass")),row(res.Columns.Get("Department")),row(res.Columns.Get("Position")),buffer2))
			End If
		Next
		ProgressDialogShow2("Account succesfully updated.", False)
		Sleep(2000)
		ProgressDialogHide
		LOAD_USER
	Else
		Log("ERROR: " & jr.ErrorMessage)
		ProgressDialogHide
		Msgbox2Async("ACCOUNT DATA IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
	jr.Release
End Sub
Sub UPDATE_SAMPLE
	ProgressDialogShow2("Updating accounts...", False)
	Dim req As DBRequestManager = CreateRequest
	Dim cmd As DBCommand = CreateCommand("select_sample", Null)
	Wait For (req.ExecuteQuery(cmd, 0, Null)) JobDone(jr As HttpJob)
	If jr.Success Then
		req.HandleJobAsync(jr, "req")
		Wait For (req) req_Result(res As DBResult)
		For Each row() As Object In res.Rows
			Msgbox(row(res.Columns.Get("principal_name")),False)
		Next
		ProgressDialogShow2("Account succesfully updated.", False)
		Sleep(2000)
		ProgressDialogHide
		LOAD_USER
	Else
		Log("ERROR: " & jr.ErrorMessage)
		ProgressDialogHide
		Msgbox2Async("ACCOUNT DATA IS NOT UPDATED." & CRLF & CRLF & "Solution:" & CRLF & "1. Make sure the device is connected on the WiFi network"& CRLF & "2. If device is already connected and error still exist, Please inform IT Depatment", "Warning", "", "", "OK", LoadBitmap(File.DirAssets, "LOGO_3D.png"), True)
	End If
	jr.Release
End Sub
#End Region

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

Sub GET_TOKEN
	connection.ExecNonQuery("DELETE FROM user_token_table")
	Sleep(0)
	connection.ExecNonQuery("INSERT INTO user_token_table VALUES ('"&SPINNER_USER.SelectedItem&"','1','"&DateTime.Date(DateTime.Now) & " " & DateTime.Time(DateTime.Now)&"','-')")
End Sub

Sub GET_LAST_LOG
	cursor3 = connection.ExecQuery("SELECT * FROM user_token_table")
	If cursor3.RowCount > 0 Then
		For row = 0 To cursor3.RowCount - 1
			cursor3.Position = row
			LABEL_LOAD_LASTLOG.Text = "Last User : " & cursor3.GetString("user") & CRLF & "Date Time Login : " & cursor3.GetString("last_login")  & CRLF & "Date Time Logout : " & cursor3.GetString("last_logout")
		Next
	End If
End Sub


