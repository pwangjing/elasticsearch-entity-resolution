/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaba.entity.cleaner;

import javafx.scene.paint.Stop;
import no.priv.garshol.duke.Cleaner;
import no.priv.garshol.duke.utils.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangj117 on 4/8/16.
 */
public class StopWordsCleaner implements Cleaner {
    private final Logger logger = Logger.getLogger(StopWordsCleaner.class);


    List<String> stopWords;
    public void setStopWords(String stopwords){

        this.stopWords= Arrays.asList( StringUtils.split(","));

    }
    @Override
    public String clean(String s) {
        String removeQuotes=s.replace(',',' ').replace("'","").replace(":\\s*"," ").replaceAll("\\(\\d+\\)","");
        String[] tokenized=StringUtils.split(removeQuotes);
        List<String> tokenziedString=new LinkedList<>(Arrays.asList(tokenized));
        tokenziedString.removeAll(stopWords);
        String ret=StringUtils.join(tokenziedString.toArray(new String[0]));

        return ret;
    }
}
