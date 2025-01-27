package net.runelite.api.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

public class Text {

    private static final StringBuilder SB = new StringBuilder(64);
    public static final Splitter COMMA_SPLITTER = Splitter
            .on(",")
            .omitEmptyStrings()
            .trimResults();

    private static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();
    public static String removeTags(String str, boolean removeLevels)
    {
        if (removeLevels)
        {
            int levelIdx =  StringUtils.lastIndexOf(str, "  (level");
            if (levelIdx >= 0)
            {
                str = str.substring(0, levelIdx);
            }
        }

        int strLen = str.length();

        int open, close;
        if ((open = StringUtils.indexOf(str, '<')) == -1
                || (close = StringUtils.indexOf(str, '>', open)) == -1)
        {
            return strLen == str.length() ? str : str.substring(0, strLen - 1);
        }

        // If the string starts with a < we can maybe take a shortcut if this
        // is the only tag in the string (take the substring after it)
        if (open == 0)
        {
            if ((open = close + 1) >= strLen)
            {
                return "";
            }

            if ((open = StringUtils.indexOf(str, '<', open)) == -1
                    || (StringUtils.indexOf(str, '>', open)) == -1)
            {
                return StringUtils.substring(str, close + 1);
            }

            // Whoops, at least we know the last value so we can go back to where we were
            // before :)
            open = 0;
        }

        SB.setLength(0);
        int i = 0;
        do
        {
            while (open != i)
            {
                SB.append(str.charAt(i++));
            }

            i = close + 1;
        }
        while ((open = StringUtils.indexOf(str, '<', close)) != -1
                && (close = StringUtils.indexOf(str, '>', open)) != -1
                && i < strLen);

        while (i < strLen)
        {
            SB.append(str.charAt(i++));
        }

        return SB.toString();
    }

    public static String removeTags(String str)
    {
        return removeTags(str, false);
    }

    public static List<String> fromCSV(final String input)
    {
        return COMMA_SPLITTER.splitToList(input);
    }

    /**
     * Joins collection of strings as comma separated values
     *
     * @param input collection
     * @return comma separated value string
     */
    public static String toCSV(final Collection<String> input)
    {
        return COMMA_JOINER.join(input);
    }

    public static String standardize(String str, boolean removeLevel)
    {
        if (StringUtils.isBlank(str))
        {
            return str;
        }

        return removeTags(str, removeLevel).replace('\u00A0', ' ').trim().toLowerCase();
    }

    public static String standardize(String str)
    {
        return standardize(str, false);
    }
}
