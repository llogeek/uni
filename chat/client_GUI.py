import random
import socket
import sys
import tkinter as tk
from tkinter import messagebox
import time
import threading
import os
from aes import aes_decrypt, aes_encrypt
from email_my import send_mail
from hashlib import sha256
from implementing_rsa import generate_keys, encrypt, decrypt, rename_key_files, read_pub_key, read_priv_key
import mysql.connector

class GUI:

    def __init__(self, ip_address, port, conn):
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.connect((ip_address, port))
        self.folder_users = ".\\clients\\"
        self.folder_keys = ".\\keys\\"
        self.Window = tk.Tk()
        self.Window.withdraw()
        self.key_get = 0
        self.login = tk.Toplevel()
        self.conn = conn
        self.login.title("Login")
        self.login.resizable(width=False, height=False)
        self.login.configure(width=400, height=350)

        self.pls = tk.Label(self.login,
                            text="Please Login to a chatroom",
                            justify=tk.CENTER,
                            font="Helvetica 12 bold")

        self.pls.place(relheight=0.15, relx=0.2, rely=0.07)

        self.userLabelName = tk.Label(self.login, text="Username: ", font="Helvetica 11")
        self.userLabelName.place(relheight=0.2, relx=0.1, rely=0.25)

        self.userEntryName = tk.Entry(self.login, font="Helvetica 12")
        self.userEntryName.place(relwidth=0.4, relheight=0.1, relx=0.35, rely=0.30)
        self.userEntryName.focus()

        self.roomLabelName = tk.Label(self.login, text="Password: ", font="Helvetica 12")
        self.roomLabelName.place(relheight=0.2, relx=0.1, rely=0.40)

        self.roomEntryName = tk.Entry(self.login, font="Helvetica 11", show="*")
        self.roomEntryName.place(relwidth=0.4, relheight=0.1, relx=0.35, rely=0.45)
        self.reg = tk.Button(self.login,
                            text="Register",
                            font="Helvetica 12 bold",
                            command=lambda: self.registerr())
        self.go = tk.Button(self.login,
                            text="Log In",
                            font="Helvetica 12 bold",
                            command=lambda: self.login_verify()) #self.goAhead(self.userEntryName.get(), self.roomEntryName.get()))
        self.reg.place(relx=0.45, rely=0.75)
        self.go.place(relx=0.47, rely=0.62)
        self.msg = ''
        self.Window.mainloop()

    def registerr(self):
        self.register_screen = tk.Toplevel()
        self.register_screen.title("Register")
        self.register_screen.geometry("300x250")

        self.username = tk.StringVar()
        self.password = tk.StringVar()
        self.email = tk.StringVar()

        tk.Label(self.register_screen, text="Please enter details below", bg="light green").pack()
        tk.Label(self.register_screen, text="").pack()
        email_lable = tk.Label(self.register_screen, text="Email * ")
        email_lable.pack()
        self.email_entry = tk.Entry(self.register_screen, textvariable=self.email)
        self.email_entry.pack()
        username_lable = tk.Label(self.register_screen, text="Username * ")
        username_lable.pack()
        self.username_entry = tk.Entry(self.register_screen, textvariable=self.username)
        self.username_entry.pack()
        password_lable = tk.Label(self.register_screen, text="Password * ")
        password_lable.pack()
        self.password_entry = tk.Entry(self.register_screen, textvariable=self.password, show='*')
        self.password_entry.pack()
        tk.Label(self.register_screen, text="").pack()
        self.confirm_registration = tk.Button(self.register_screen,
                            text="Confirm Register",
                            font="Helvetica 12 bold",
                            command=lambda: self.register_user())
        self.confirm_registration.place(relx=0.262, rely=0.70)

    def register_user(self):
        temp_txt = ''
        username_info = self.username_entry.get()
        password_info = self.password_entry.get()
        email_info = self.email_entry.get()
        mycursor = self.conn.cursor()
        sql = "SELECT * FROM users WHERE username = %s"
        vals = [username_info]
        mycursor.execute(sql, vals)
        myresult = mycursor.fetchall()
        mycursor.close()
        if username_info != '' and password_info != '' and email_info != '':
            if myresult != None:
                cursor = self.conn.cursor()
                my_query = 'INSERT into users(username, passwordd, email) VALUES (%s, %s, %s)'
                val = (username_info, sha256(bytes(password_info, 'utf-8')).hexdigest(), email_info)
                cursor.execute(my_query, val)
                self.conn.commit()
                print(cursor.rowcount, "record inserted.")
            else:
                messagebox.showerror('User Error', 'Error: Such user already exists!')
        else:
            messagebox.showerror('User Error', 'Error: Empty information!')
        self.email_entry.delete(0, tk.END)
        self.username_entry.delete(0, tk.END)
        self.password_entry.delete(0, tk.END)
        if temp_txt == "Registration success":
            tk.Label(self.register_screen, text=temp_txt, fg="green", font=("calibri", 11)).pack()
        else:
            tk.Label(self.register_screen, text=temp_txt, fg="red", font=("calibri", 11)).pack()
        self.register_screen.destroy()

    def login_verify(self):
        username1 = self.userEntryName.get()
        password1 = sha256(bytes(self.roomEntryName.get(), 'utf-8')).hexdigest()
        self.userEntryName.delete(0, tk.END)
        self.roomEntryName.delete(0, tk.END)
        mycursor = self.conn.cursor()
        sql = "SELECT * FROM users WHERE username = %s"
        vals = [username1]
        mycursor.execute(sql, vals)
        myresult = mycursor.fetchone()
        mycursor.close()
        if myresult != None:
            if username1 == myresult[1] and password1 == myresult[2]:
                self.key_temperate = random.randint(1000, 9999)
                send_mail(myresult[3], self.key_temperate)
                self.current_username = tk.StringVar()
                self.current_username.set(username1)
                self.current_email = tk.StringVar()
                self.current_email.set(myresult[3])
                self.inner_check(username1)
            else:
                self.password_not_recognised()
        else:
            self.user_not_found()

    def inner_check(self, username_input):
        self.check_screen = tk.Toplevel()
        self.check_screen.title("Verify code")
        self.check_screen.geometry("300x250")
        tk.Label(self.check_screen, text="Please enter details below to check code").pack()
        tk.Label(self.check_screen, text="").pack()

        self.email_verify = tk.StringVar()

        tk.Label(self.check_screen, text="Input send code * ").pack()
        self.email_login_entry = tk.Entry(self.check_screen, textvariable=self.email_verify)
        self.email_login_entry.pack()
        tk.Button(self.check_screen,
                  text="Confirm",
                  font="Helvetica 12 bold",
                  command=lambda: self.check(username_input)).pack()

    def check(self, username):
        code1 = self.email_verify.get()
        self.email_login_entry.delete(0, tk.END)
        if str(self.key_temperate) == code1:
            self.login_sucess(username)
        else:
            self.password_not_recognised()
        self.check_screen.destroy()

    def login_sucess(self, username):
        self.login_user_info = tk.Toplevel()
        self.login_user_info.title("Profile")
        self.login_user_info.geometry("250x200")
        generate_keys(username)
        self.user_label = tk.Label(self.login_user_info, text="Name: {}".format(self.current_username.get()))
        self.user_label.pack()
        self.email_label = tk.Label(self.login_user_info, text="Email: {}".format(self.current_email.get()))
        self.email_label.pack()
        self.join_chat = tk.Button(self.login_user_info,
                  text="Chat",
                  font="Helvetica 12 bold",
                  command=lambda: self.goAhead(self.current_username.get()))
        self.edit_info = tk.Button(self.login_user_info,
                  text="Edit information",
                  font="Helvetica 12 bold",
                  command=lambda: self.edit_profile(self.current_username.get()))
        self.join_chat.place(relx=0.4, rely=0.6)
        self.edit_info.place(relx=0.245, rely=0.4)

    def edit_profile(self, username):
        self.edit_info_screen = tk.Toplevel()
        self.edit_info_screen.title("Edit profile information")
        self.edit_info_screen.geometry("300x250")

        self.username = tk.StringVar()
        self.password = tk.StringVar()
        self.email = tk.StringVar()

        tk.Label(self.edit_info_screen, text="Please enter details below", bg="light green").pack()
        tk.Label(self.edit_info_screen, text="").pack()
        email_lable = tk.Label(self.edit_info_screen, text="Email * ")
        email_lable.pack()
        self.email_entry = tk.Entry(self.edit_info_screen, textvariable=self.email)
        self.email_entry.pack()
        username_lable = tk.Label(self.edit_info_screen, text="Username * ")
        username_lable.pack()
        self.username_entry = tk.Entry(self.edit_info_screen, textvariable=self.username)
        self.username_entry.pack()
        password_lable = tk.Label(self.edit_info_screen, text="Password * ")
        password_lable.pack()
        self.password_entry = tk.Entry(self.edit_info_screen, textvariable=self.password, show='*')
        self.password_entry.pack()
        tk.Label(self.edit_info_screen, text="").pack()
        self.confirm_editing = tk.Button(self.edit_info_screen,
                                              text="Edit",
                                              font="Helvetica 12 bold",
                                              command=lambda: self.edit_user_info(username))
        self.confirm_editing.place(relx=0.43, rely=0.70)

    def edit_user_info(self, username_):
        username_info = self.username_entry.get()
        password_info = self.password_entry.get()
        email_info = self.email_entry.get()
        mycursor = self.conn.cursor()
        sql = "SELECT id from users WHERE username = %s"
        vals = [username_]
        mycursor.execute(sql, vals)
        idd = mycursor.fetchone()[0]
        self.conn.commit()
        if username_info != '':
            sql = "UPDATE users SET username = %s WHERE id = %s"
            vals = [username_info, idd]
            mycursor.execute(sql, vals)
            self.conn.commit()
            self.current_username.set(username_info)
            self.user_label.configure(text="Name: {}".format(self.current_username.get()))
        if email_info != '':
            sql = "UPDATE users SET email = %s WHERE id = %s"
            vals = [email_info, idd]
            mycursor.execute(sql, vals)
            self.conn.commit()
            self.current_email.set(email_info)
            self.email_label.configure(text="Email: {}".format(self.current_email.get()))
        if password_info != '':
            sql = "UPDATE users SET passwordd = %s WHERE id = %s"
            vals = [sha256(bytes(password_info, 'utf-8')).hexdigest(), idd]
            mycursor.execute(sql, vals)
            self.conn.commit()
        mycursor.close()
        self.email_entry.delete(0, tk.END)
        self.username_entry.delete(0, tk.END)
        self.password_entry.delete(0, tk.END)
        self.edit_info_screen.destroy()

    def code_not_recognised(self):
        self.code_not_recog_screen = tk.Toplevel()
        self.code_not_recog_screen.title("Failed")
        self.code_not_recog_screen.geometry("150x100")
        tk.Label(self.code_not_recog_screen, text="Invalid Code ").pack()
        tk.Button(self.code_not_recog_screen,
                  text="OK",
                  font="Helvetica 12 bold",
                  command=lambda: self.delete_code_not_recognised()).pack()

    # Designing popup for user not found

    def user_not_found(self):
        self.user_not_found_screen = tk.Toplevel()
        self.user_not_found_screen.title("Success")
        self.user_not_found_screen.geometry("150x100")
        tk.Label(self.user_not_found_screen, text="User Not Found").pack()
        tk.Button(self.user_not_found_screen,
                  text="OK",
                  font="Helvetica 12 bold",
                  command=lambda: self.delete_user_not_found_screen()).pack()

    def password_not_recognised(self):
        self.password_not_recog_screen = tk.Toplevel()
        self.password_not_recog_screen.title("Failed")
        self.password_not_recog_screen.geometry("150x100")
        tk.Label(self.password_not_recog_screen, text="Invalid Password ").pack()
        tk.Button(self.password_not_recog_screen,
                  text="OK",
                  font="Helvetica 12 bold",
                  command=lambda: self.delete_password_not_recognised()).pack()

    def delete_password_not_recognised(self):
        self.password_not_recog_screen.destroy()

    def delete_code_not_recognised(self):
        self.code_not_recog_screen.destroy()

    def delete_user_not_found_screen(self):
        self.user_not_found_screen.destroy()


    def goAhead(self, username, room_id=str(0)):
        self.login_user_info.destroy()
        self.name = username
        self.server.send(str.encode(username))
        time.sleep(0.1)
        self.server.send(str.encode(room_id))

        self.login.destroy()
        self.layout()

        rcv = threading.Thread(target=self.receive)
        rcv.start()

    def layout(self):
        self.Window.deiconify()
        self.Window.title("CHATROOM")
        self.Window.resizable(width=False, height=False)
        self.Window.configure(width=470, height=400, bg="#17202A")
        self.chatBoxHead = tk.Label(self.Window,
                                    bg="#17202A",
                                    fg="#EAECEE",
                                    text=self.name,
                                    font="Helvetica 11 bold",
                                    pady=5)

        self.chatBoxHead.place(relwidth=1)

        self.line = tk.Label(self.Window, width=350, bg="#ABB2B9")

        self.line.place(relwidth=1, rely=0.07, relheight=0.012)

        self.textCons = tk.Text(self.Window,
                                width=20,
                                height=2,
                                bg="#17202A",
                                fg="#EAECEE",
                                font="Helvetica 11",
                                padx=5,
                                pady=5)

        self.textCons.place(relheight=0.745, relwidth=1, rely=0.08)

        self.labelBottom = tk.Label(self.Window, bg="#ABB2B9", height=60)

        self.labelBottom.place(relwidth=1,
                               rely=0.8)

        self.entryMsg = tk.Entry(self.labelBottom,
                                 bg="#2C3E50",
                                 fg="#EAECEE",
                                 font="Helvetica 11")
        self.entryMsg.place(relwidth=0.74,
                            relheight=0.03,
                            rely=0.008,
                            relx=0.011)
        self.entryMsg.focus()

        self.buttonMsg = tk.Button(self.labelBottom,
                                   text="Send",
                                   font="Helvetica 10 bold",
                                   width=20,
                                   bg="#ABB2B9",
                                   command=lambda: self.sendButton(self.entryMsg.get()))

        self.buttonMsg.place(relx=0.77,
                             rely=0.008,
                             relheight=0.03,
                             relwidth=0.22)
        self.textCons.config(cursor="arrow")
        scrollbar = tk.Scrollbar(self.textCons)
        scrollbar.place(relheight=1,
                        relx=0.974)

        scrollbar.config(command=self.textCons.yview)
        self.textCons.config(state=tk.DISABLED)

    def sendButton(self, msg):
        self.textCons.config(state=tk.DISABLED)
        self.msg = msg
        if self.msg.upper() == 'QUIT':
            self.msg = 'User left chat'
        self.entryMsg.delete(0, tk.END)
        snd = threading.Thread(target=self.sendMessage)
        snd.start()

    def receive(self):
        while True:
            if self.msg == 'User left chat':
                break
            try:
                message = self.server.recv(1024)
                if self.key_get == 0:
                    pt = decrypt(self.name, message)
                    self.aes_key = pt
                    self.key_get = 1
                else:
                    pt = aes_decrypt(message, self.aes_key).decode('utf-8', 'ignore')
                    self.textCons.config(state=tk.DISABLED)
                    self.textCons.config(state=tk.NORMAL)
                    self.textCons.insert(tk.END, pt + "\n\n")
                    self.textCons.config(state=tk.DISABLED)
                    self.textCons.see(tk.END)
            except:
                print("User disconnected!")
                self.server.close()
                break
        self.Window.destroy()

    def sendMessage(self):
        self.textCons.config(state=tk.DISABLED)
        while True:
            to_send = aes_encrypt(self.msg, self.aes_key)
            self.server.send(to_send)
            self.textCons.config(state=tk.NORMAL)
            self.textCons.insert(tk.END,
                                 "You: " + self.msg + "\n\n")
            self.textCons.config(state=tk.DISABLED)
            self.textCons.see(tk.END)
            break

def create_connection():
    conn = mysql.connector.connect( user='Lolita',
                                    password = 'Lolita23102002' ,
                                    host = "localhost" ,
                                    database = 'users_db')
    return conn

if __name__ == "__main__":
    connection = create_connection()
    ip_address = "127.0.0.1"
    port = 12345
    g = GUI(ip_address, port, connection)
    connection.close()
    sys.exit(0)
