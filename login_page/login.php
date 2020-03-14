<?php
include 'dbconfig.php';
$conn=mysqli_connect($host,$user,$pass,$db) or die("we couldn't connect");
$username=$_POST['user_name'];
$password=$_POST['password'];
$result=mysqli_query($conn,"SELECT * from user_details where username='$username' and role=1 ");
if(mysqli_num_rows($result)>0 )
{
  $data=mysqli_fetch_array($result);
  $pass=$data['password'];
  if(password_verify($password,$pass))
  {
    mysqli_close($conn);
  	echo "success";
  }
  else
  {
    mysqli_close($conn);
    echo "wrong username/password";
  }
}
else
{
    mysqli_close($conn);
  	echo "wrong username/password";
}
?>
