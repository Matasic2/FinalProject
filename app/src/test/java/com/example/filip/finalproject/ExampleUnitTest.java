package com.example.filip.finalproject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testing_attacks(){
        Units attacker = new Units();
        Units defender = new Units();

        attacker.HP = 2;
        defender.HP = 4;

        attacker.attack2Range = 10;
        attacker.attack2 = 3;

        defender.attack2Range = 10;
        defender.attack2 = 4;

        attacker.defence = 0;
        defender.defence = 0;
        attacker.attack1Range = 1;
        attacker.attack1Range = 1;

        GameEngine.BoardSprites[0][2] = attacker;
        GameEngine.BoardSprites[0][0] = defender;

        GameEngine.attackUnit(attacker, defender);
        assert (defender.HP == 3);
    }
}