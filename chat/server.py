import os
import socket
from _thread import *
from collections import defaultdict as df
from implementing_rsa import decrypt, encrypt, read_priv_key, read_pub_key
from aes import aes_decrypt, aes_encrypt


class Server:
    def __init__(self):
        self.rooms = df(list)
        self.folder_users = ".\\clients\\"
        self.folder_keys = ".\\keys\\"
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    def accept_connections(self, ip_address, port):
        self.ip_address = ip_address
        self.port = port
        self.server.bind((self.ip_address, int(self.port)))
        self.server.listen(100)

        while True:
            connection, address = self.server.accept()
            print(str(address[0]) + ":" + str(address[1]) + " Connected")
            start_new_thread(self.clientThread, (connection,))
        self.server.close()

    def clientThread(self, connection):
        user_id = connection.recv(1024).decode().replace("User ", "")
        room_id = connection.recv(1024).decode().replace("Join ", "")
        aes_key = os.urandom(16)
        key_to_send = encrypt(user_id, aes_key)
        connection.send(key_to_send)
        self.rooms[room_id].append([connection, user_id, aes_key])
        while True:
            try:
                message = connection.recv(1024)
                if message:
                    ms = aes_decrypt(message, aes_key).decode('utf-8', 'ignore')
                    message_to_send = str(user_id) + ": " + str(ms)
                    self.broadcast(message_to_send, connection, room_id)
                else:
                    self.remove(connection, room_id)
            except Exception as e:
                print(repr(e))
                print("Client disconnected earlier")
                break


    def broadcast(self, message_to_send, connection, room_id):
        for client in self.rooms[room_id]:
            if client[0] != connection:
                try:
                    client[0].send(aes_encrypt(message_to_send, client[2]))
                except:
                    client[0].close()
                    self.remove(client[0], room_id)

    def remove(self, connection, room_id):
        if connection in self.rooms[room_id]:
            self.rooms[room_id].remove(connection)


if __name__ == "__main__":
    ip_address = "127.0.0.1"
    port = 12345
    s = Server()
    s.accept_connections(ip_address, port)