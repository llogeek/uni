import os

from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA

folder_keys = ".\\keys\\"

def rename_key_files(old_username, new_username):
    os.rename(folder_keys + old_username + "_private.pem", folder_keys + new_username + "_private.pem")
    os.rename(folder_keys + old_username + "_public.pem", folder_keys + new_username + "_public.pem")

def generate_keys(username):
    private_key = RSA.generate(1024)
    public_key = private_key.publickey()

    f = open(folder_keys + username + "_private.pem", 'wb')
    f.write(private_key.exportKey('PEM'))
    f.close()
    f = open(folder_keys + username + "_public.pem", 'wb')
    f.write(public_key.exportKey('PEM'))
    f.close()

    return private_key, public_key

def read_priv_key(username):
    f = open(folder_keys + username + "_private.pem", 'rb')
    key = RSA.import_key(f.read())
    f.close()
    return key

def read_pub_key(username):
    f = open(folder_keys + username + "_public.pem", 'rb')
    key = RSA.import_key(f.read())
    f.close()
    return key

def encrypt(username, message):
    f = open(folder_keys + username + "_public.pem", 'rb')
    key = RSA.import_key(f.read())
    f.close()
    cipher = PKCS1_OAEP.new(key)
    ciphertext = cipher.encrypt(message)
    return ciphertext

def decrypt(username, ciphertext):
    f = open(folder_keys + username + "_private.pem", 'rb')
    key = RSA.import_key(f.read())
    f.close()
    cipher = PKCS1_OAEP.new(key)
    decrypted_message = cipher.decrypt(ciphertext)
    return decrypted_message