# AsciiArtGenerator
Converts an image into ascii art.
Created as a program demo for Intermediate Java students to learn from.

Features:
- Edge detection using the Sobel operator
- Contrast boost for clearer images, using a sigmoid function

Demo w/o edges:
```
                       --/ahhhkb/      l*qqq*m                             
                   -/#ooaa-                   ZZZmm`                       
                /MM##oo-                         LL0ZZm                    
             /WWWMM#o                              zCLQ0OO                 
           &&&WWWMM/                                 CCCLQOO|              
         &&&&&WWWM``                                  CCLLLQLQr            
       &&&&&&&WWW|``                                   LQLLLLLLQ;          
      |&&&&&&&&WW````                                  0QLLLLLLLQ          
    ``|&&&&&&&&&W`````                                 OOOO000000          
   ```|&&&&&&&&&&(.`````                               mZZOOOOOOO          
  ````&888&&&&&&&&..```````                           *wwwmmmmmmZl         
 `````88888&&&&&&&&...`````````                      ddppq**wwwwww         
 ````|8888888&&&&&&&\....``````````                /hkkkbbdppqq***         
`````k8888888888888888\.......``````````         /oooaaahhkkbddddp         
`````88888888888888888888\-.........`````````-/MMMMM###ooaahhhkkkbb    `   
````|888888888888888888888888&\----------/&&&WWWWWWMMMM#####oooaaaa    `   
 ```8888888888888%888888888888888888888888&&&&&&&&&WWWWWMMMMMM#####|````   
 ```&8888888888%%%%%%%%%%%%%%%%%%%%%%%%%%888888888&&&&&&&WWWWWWMMMMM````   
```W&&8888888888%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%888888&&&&&&&WWWWWW(```   
  `&&&8888888888%%%%%%%%%%%BBBBBBBBBBBBBB%%%%%%%%%%%%%8888888&&&&&&&&``    
  &&&&&8888888%%%%%%BBBBh*Lx/{+>i_,^````":-li--YB%%%%%%%%%%%888888&&&&`    
  W&&&&&8888%%%%BBBpJj([+<_&|||..````````$ w````.,--CB%%%%%%%%%%88888&     
   &&&&&888%%%BBL|+>!l_,^``@%8B.`````````$&B|      ``,iXB%%%%%%%%%%%8      
    \&&888%%%Bz>-_;;,"^^^``@$$B.``````` `@$$|         `.!WB%%%%%%%%        
      -&888%%B:""",,,"""^^^@@@|..````````@$$.          ``|BB%%%%8          
         -&8%%^`^^"",,:,,""^\/`''....`````--`````````````|%%%b-            
             -```^^"",::;_;:,""^^``'''......``````````..'                  
               ''`^^",,:;_----_;:,,""^^```''''''..'''''`                   
                 '`^"",::;_-lllllllllllll-----__----ll!                    
                    ^,;_-ll!i>>>>>>>><>><><><<<<<I<.                       
                                `.`^^'.`                                   
```

Demo w/ edges:
```
                 ----|      |----                
            /-/---/--        --\-/-\-            
         //-/ ///                \\\ -\-         
       -//   )/                    \(   -\\      
      |)     |                      |     \|     
      |      |                      |      |     
      |      (\                    /)      (     
     |)       \\\                ///       |     
     |          ----\        /---/          |    
     |              \--------/              (    
    )                                       \|   
   )|               /--------                \(  
   |\         -----||(      //|----\         /)  
    -\\    //                 |     -\     -//   
      \-\- |       ( )      ( )       |  /-/     
          \-                          -          
             \--                  --/            

```

Original:

![mushroom](https://user-images.githubusercontent.com/56645274/127963708-118121f0-1130-4536-b41a-6427c3f8d026.PNG)
