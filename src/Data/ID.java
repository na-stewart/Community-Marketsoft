package Data;

import java.util.Random;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class ID {
    public int getId(){
        Random rand = new Random();
        return rand.nextInt(999999);
    }
}
