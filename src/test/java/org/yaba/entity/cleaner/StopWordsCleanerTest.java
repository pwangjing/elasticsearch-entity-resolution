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

import no.priv.garshol.duke.Cleaner;
import no.priv.garshol.duke.comparators.WeightedLevenshtein;
import no.priv.garshol.duke.utils.ObjectUtils;
import org.junit.Test;
import org.yaba.entity.comparator.DiceCoefficentShingleComparator;

import java.util.Collections;

/**
 * Created by wangj117 on 4/8/16.
 */
public class StopWordsCleanerTest {

    @Test
    public void testClean() throws Exception {

        System.out.println(ObjectUtils.makePropertyName("stop-words"));
        Cleaner cleaner= (Cleaner) ObjectUtils.instantiate("org.yaba.entity.cleaner.StopWordsCleaner");
        ObjectUtils.setBeanProperty(cleaner,"stop-words","the", Collections.<String, Object>emptyMap());


        String actual=cleaner.clean("Metropolitan (2015 re-release) The Boss, Boss, the,  Teenage Mutant Ninja Turtles 2  , Teenage Mutannt Ninja Turtles: Out of the Shadows ");
        System.out.println(actual);

    }
    @Test
    public void testDicedCoeffienct(){
        DiceCoefficentShingleComparator comparator=new DiceCoefficentShingleComparator();

        comparator.setComparator(new WeightedLevenshtein());
        double score=comparator.compare("Teenage Mutant Ninja Turtles 2"  , "Teenage Mutannt Ninja Turtles: Out of the Shadows" );
        System.out.println(score);
         score=comparator.compare("Teenage Mutannt Ninja Turtles: Out of the Shadows","Deeper Shade Of Blue, A (NCM)" );
        System.out.println(score);
        score=comparator.compare("Peanuts","Tenants" );
        System.out.println(score);

        score=comparator.compare("fury (2014)","fury" );

        System.out.println(score);


    }
}