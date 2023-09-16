
# <p align="center">GO4LUNCH</p>

Application developed for the P7 of my Android developer formation with OpenClassrooms
  
Go4Lunch is a application designed for workplace teams. It simplifies the process of finding a nearby restaurant, selecting one's choice, and coordinating lunch plans with colleagues. Just before lunchtime, the app sends notifications to remind users to join their coworkers for a lunch break.
    
## 🧐 Features    
#### Authentication: 
Secure user authentication is implemented with Gmail, facebook, Twitter or personnal email account

![Authentification](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/ab3e299f-2304-4d8e-9ccf-a772e63ebc65)

#### The application is made up of three main views : 
- The view of restaurants in map form;
- The view of restaurants in list form;
- The view of colleagues using the application
- 
 ![mapview](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/9f667788-98fd-4807-b787-bcb0e5b855c9)
 ![listview](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/39cc4745-1ed0-4399-848e-0443276a01ca)
 ![workmatesview](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/195a18ed-38a3-4e7b-b115-930bd1370a1e)


#### Detailed restaurant information :
When the user clicks on a restaurant (from the map or from the
list), a new screen appears to display the details of this restaurant. The
information displayed repeats that of the list.

![detail](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/28a19872-714b-46f5-93fe-ad513f41c171)

#### List of colleagues :
This screen displays the list of all your colleagues, with their choice of
restaurant. If a colleague has chosen a restaurant, you can press
above (on the screen, not on your colleague) to display the detailed file
of this restaurant.

#### Search functionality :
On each view, a magnifying glass located at the top right of the screen allows
to display a search box

![search](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/44ff79e4-d87c-48f8-9ef0-1bcb2ba7d75b)

#### Drawer :
At the top left is a menu button. By clicking on it, a
side menu is displayed, with the following information:
- Your profile photo;
- Your first and last name, A button to display the
restaurant where you plan to go for lunch;
- A button to access the settings screen.
- A button to log out and return to the screen
connection.

![drawer](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/1f8c71b8-69f0-4f48-91d6-3a20f2ddb427)

#### Notifications:
A notification message is automatically sent to all
users who have selected a restaurant in the application. THE
message will be sent at 12 p.m. It will remind the user of the name of the restaurant
that he has chosen, the address, as well as the list of colleagues who will come with him.

![notification](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/d5350ced-24bf-4873-b44e-ec4b47b57bb5)

#### Translation :
Colleagues being of all nationalities, the application
offers a French and English version of the application.

![translation](https://github.com/LaurentVrevin/Go4lunch/assets/94620399/d0de68e3-87e7-47f6-97d7-1a19e0cecb8e)

#### Architecture    
MVVM : Repository / ViewModel / LiveData 

## 🛠️ Tech Stack
- [Firebase Auth](https://firebase.google.com/docs/auth)
- [Firebase Firestore](https://firebase.google.com/docs/firestore)
- [Facebook SDK](https://developers.facebook.com/docs/facebook-login/android)
- [Twitter SDK](https://developer.twitter.com/en/docs/authentication/guides/log-in-with-twitter)
- [EasyPermissions](https://github.com/googlesamples/easypermissions)
- [Retrofit](https://square.github.io/retrofit/)
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=fr)
- [Glide](https://bumptech.github.io/glide/)
- [Mockito](https://site.mockito.org/)

    
        

        
