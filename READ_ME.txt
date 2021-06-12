Guild Gendata
Mở folder genRealDataTCP để sử dụng gen bằng TCP 
Code Gendata trong package main
B1: run file TCPServerView để tạo 2 server bắn 2 loại dữ liệu khác nhau cùng 1 lúc
B2: run file TCPClientView để tạo luồng hứng dữ liệu đồng thời từ 2 server 

TCPClientView cồm 3 Thread
Thread 1  in ra dữ liệu nhận đc  từ Server 1
Thread 2  in ra dữ liệu nhận đc  từ Server 2 
Thread 3  in ra  tổng số dữ liệu /s nhận đc từ cả 2 server

