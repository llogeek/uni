import base64
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad,unpad

def aes_encrypt(raw, key):
        raw = pad(raw.encode(),16)
        cipher = AES.new(key, AES.MODE_ECB)
        return base64.b64encode(cipher.encrypt(raw))

def aes_decrypt(enc, key):
        enc = base64.b64decode(enc)
        cipher = AES.new(key, AES.MODE_ECB)
        return unpad(cipher.decrypt(enc),16)
