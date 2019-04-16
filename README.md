# Keyboard
自定义支持拖动、支持缩放的软键盘。适用于Activity、Fragment、DialogFragment。

### 一、Screenshots
 + 1、数字键盘
 ![number_keyboard](output/shots/number_keyboard.png)
 
 + 2、字母键盘
 ![number_keyboard](output/shots/letter_keyboard.png)
  
+ 3、字母、数字混合键盘
 ![number_keyboard](output/shots/letter_number_keyboard.png)
 
### 二、Usage
 使用要点：
 + a、创建[KeyBoardView](keboardLibrary/src/main/java/jsc/kit/keyboard/KeyBoardView.java)实例：
```
KeyBoardView keyboardView = new KeyBoardView(context);
```
+ b、管理所有需要使用该自定义键盘的`EditText`：
```
    //如果view是ViewGroup，自动查找该ViewGroup树下的所有EditText并加入管理
    public void addAllInputView(View view)

    //添加某一特定的EditText
    public void addInputView(@NonNull EditText editText)

    public void removeAllInputView(View view)

    public void removeInputView(@NonNull EditText editText)
```
+ c、把[KeyBoardView](keboardLibrary/src/main/java/jsc/kit/keyboard/KeyBoardView.java)添加到`Activity`、`Fragment`、`DialogFragment`所在的`Window`中：
[KeyUtils](keboardLibrary/src/main/java/jsc/kit/keyboard/KeyUtils.java)工具已提供了一个快速添加的方法。
```
KeyUtils.init(getActivity().getWindow(), keyboardView);
```
 
 + 1、Activity、Fragment
 

### LICENSE
```
   Copyright 2019 JustinRoom

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
