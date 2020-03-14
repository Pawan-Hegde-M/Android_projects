<?php
include 'dbconfig.php';
$conn=mysqli_connect($host,$user,$pass,$db) or die("we couldn't connect");
$username=$_POST['user_name'];
$password=$_POST['password'];
$password2=password_hash($password,PASSWORD_BCRYPT);
$name=$_POST['name'];
$phone=$_POST['phone'];
$email=$_POST['email'];
$result=mysqli_query($conn,"INSERT into user_details(username,password,name,phone_no,email,role) VALUES('$username','$password2','$name','$phone','$email',1)");
if ($result) 
{
  mysqli_close($conn);
  echo "success";
}
else
{
    mysqli_close($conn);
  	echo "Failed";
}
?>