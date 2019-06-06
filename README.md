# HelU
https://play.google.com/store/apps/details?id=com.pale_cosmos.helu

HELU is a real-time random chat application that allows you to chat with unspecified students with your desired university and department.

## Contents
### 1. Sign in & Sign up
Your information is stored in Firebase Authentication and Realtime Database.
you should your e-mail, password, nickname, phone number, university, department and gender.
Firebase Authentication makes your identification number (We call it 'Key' or 'UID')

<div>
  <img src="https://user-images.githubusercontent.com/43880597/59008198-a94cca80-8864-11e9-9034-b17d29590cb7.png" width="30%"></img>
   <img src="https://user-images.githubusercontent.com/43880597/59008201-abaf2480-8864-11e9-912e-2aabd5813e20.png" width="30%"></img>
    <img src="https://user-images.githubusercontent.com/43880597/59008204-ace05180-8864-11e9-921c-f3dc5278687f.png" width="30%"></img>
</div>

### 2. U chat (Realtime Random Chatting)
In this case, we use your key to find your partner.
when you start chat, HELU sends your information (Key, University, Department) to HELU subServer.
The subServer (writed in JAVA) matches you and your partner whose information you want.
Your message information will be removed when you exit U chat.

### 3. Friend 
After You exit U chat, If you want, You can add partner in your friends.
Once your partner is added in your friends, You can't delete your partner in your friends.
You can chat with your partner and call partner's mobile.
If you installed "W3W_Map" in your mobile, You can use our information to trace friend's location in "W3W_Map"

### 4. Talk
As with U chat, you can send text message or photo.
But it never be removed automatically, You can remove the information in 'Chat' tap when you want.

### 5. Qua-Talk (my Deaprtment chatting)
Qua-Talk is chatting with students in your department.
The information never be removed.

## License


