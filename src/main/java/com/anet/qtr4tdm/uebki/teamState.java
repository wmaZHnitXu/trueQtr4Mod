package com.anet.qtr4tdm.uebki;

public enum teamState {
    blue,
    red,
    green,
    yellow,
    black,
    white,
    specs;
    @Override
    public String toString() {
        switch (this) {
            case blue: return "Синие";
            case red: return "Красные";
            case green: return "Зялёные";
            case yellow: return "Жолтые";
            case white: return "Белые";
            case black: return "Чорные";
            default: return "Спектры";
        }
    }

    public String forTitleColor () {
        switch (this) {
            case blue: return "blue";
            case red: return "red";
            case green: return "green";
            case yellow: return "yellow";
            case white: return "white";
            case black: return "black";
            default: return "grey";
        }
    }
}
