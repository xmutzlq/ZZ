package com.bop.zz.widget.dialog.interfaces;

public abstract class MyDialogListener {
    public abstract void onFirst();
    public abstract void onSecond();
    public void onThird(){}

    public void onCancle(){}

    public void onGetInput(CharSequence input1,CharSequence input2){

    }

    public void onGetChoose(int chosen,CharSequence chosenTxt){

    }

    public void onGetChoose(boolean[] states){

    }


}
