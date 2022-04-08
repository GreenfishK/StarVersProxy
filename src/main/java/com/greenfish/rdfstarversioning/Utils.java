package com.greenfish.rdfstarversioning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static String readAllBytes(String resourceName) {
        String text = "";
        try (InputStream in = Proxy.class.getResourceAsStream("/" +resourceName)) {
            assert in != null;
            text = new BufferedReader(
                    new InputStreamReader(in))
                    .lines()
                    .collect(Collectors.joining("\n"));
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }


    /*//from w w w. ja v a  2 s .  co m
     * Copyright 1999-2101 Alibaba Group Holding Ltd.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */


    static byte[] rtrim(byte[] array) {
        int notZeroLen = array.length;
        for (int i = array.length - 1; i >= 0; --i, notZeroLen--) {
            if (array[i] != 0) {
                break;
            }
        }

        if (notZeroLen != array.length) {
            array = Arrays.copyOf(array, notZeroLen);
        }

        return array;
    }
}
