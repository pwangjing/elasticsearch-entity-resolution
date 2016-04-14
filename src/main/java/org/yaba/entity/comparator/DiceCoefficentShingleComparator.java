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
package org.yaba.entity.comparator;

import no.priv.garshol.duke.Comparator;
import no.priv.garshol.duke.comparators.ExactComparator;
import no.priv.garshol.duke.utils.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of the Dice coefficient using exact matching by
 * default, but can be overridden to use any sub-comparator.
 */
public class DiceCoefficentShingleComparator implements Comparator {

    private final Logger logger = Logger.getLogger(this.getClass());

    private Comparator subcomp;

    public DiceCoefficentShingleComparator() {
        this.subcomp = new ExactComparator();
    }

    int size=3;
    public void setSize(int size){
        this.size=size;
    }

    public void setComparator(Comparator comp) {
        this.subcomp = comp;
    }

    public boolean isTokenized() {
        return true;
    }

    public double compare(String s1, String s2) {
        if (s1.equals(s2))
            return 1.0;

        // tokenize
        String[] t1 = toNGrame(s1);
        logger.info("t1:"+ Arrays.asList(t1));
        String[] t2 = toNGrame(s2);
        logger.info("t2:"+Arrays.asList(t2));
        // ensure that t1 is shorter than or same length as t2
        if (t1.length > t2.length) {
            String[] tmp = t2;
            t2 = t1;
            t1 = tmp;
        }

        int continiuesMatch=0;

        // find best matches for each token in t1
        double sum = 0;
        double lastsum=0;
        for (int ix1 = 0; ix1 < t1.length; ix1++) {
            double highest = 0;
            for (int ix2 = 0; ix2 < t2.length; ix2++) {
                highest = Math.max(highest, subcomp.compare(t1[ix1], t2[ix2]));
          //      System.out.println(highest + " " + t1[ix1] + " " + t2[ix2] + " ");
            }
            //jing added, so exact match of a few words is better than fuzzy match on more words.
            if(highest < 1) highest=highest/2;
            lastsum=sum;
            sum += highest;
        }

        return Math.min(1,(sum * 2) / (t1.length + t2.length));
    }

    public String[] toNGrame(String s){
        List<String> ngramList=new ArrayList<String>();
        for(int i=1 ;i <= size;i++) {
            NgramIterator ngramIterator = new NgramIterator(i, s);
            while (ngramIterator.hasNext()) {
                ngramList.add(ngramIterator.next());
            }
        }
        return ngramList.toArray(new String[]{});
    }
    class NgramIterator implements Iterator<String> {

        String[] words;
        int pos = 0, n;

        public NgramIterator(int n, String str) {
            this.n = n;
            words = StringUtils.split(str);
        }

        public boolean hasNext() {
            return pos < words.length - n + 1;
        }

        public String next() {
            StringBuilder sb = new StringBuilder();
            for (int i = pos; i < pos + n; i++)
                sb.append((i > pos ? " " : "") + words[i]);
            pos++;
            return sb.toString();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}