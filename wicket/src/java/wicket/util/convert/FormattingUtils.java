/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.convert;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

import java.util.Locale;

import wicket.WicketRuntimeException;

/**
 * Utiltiy for formatting that works with the given instance of converter registry.
 */
public final class FormattingUtils
{
    /** converter registry to use. */
    private final ConverterRegistry converterRegistry;

    /**
     * Construct.
     * @param registry the converter registry to be used by this utils instance
     */
    FormattingUtils(ConverterRegistry registry)
    {
        this.converterRegistry = registry;
    }

    /**
     * Convert the specified value into a String. If the specified value is an array, the
     * first element (converted to a String) will be returned. The registered
     * {@link IConverter}for the <code>java.lang.String</code> class will be used, which
     * allows applications to customize Object->String conversions (the default
     * implementation simply uses toString()).
     * @param value Value to be converted (may be null)
     * @return the value converted to a string
     */
    private String convertToString(Object value)
    {
        if (value == null)
        {
            return ((String) null);
        }
        else if (value.getClass().isArray())
        {
            if (Array.getLength(value) < 1)
            {
                return (null);
            }

            value = Array.get(value, 0);

            if (value == null)
            {
                return ((String) null);
            }
            else
            {
                try
                {
                    IConverter converter = this.converterRegistry.lookup(String.class);
                    Object converted = converter.convert(value);

                    return (converted instanceof String) ? (String) converted : String
                            .valueOf(converted);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    throw new ConversionException(e);
                }
            }
        }
        else
        {
            try
            {
                IConverter converter = this.converterRegistry.lookup(String.class);
                Object converted = converter.convert(value);

                return (converted instanceof String) ? (String) converted : String
                        .valueOf(converted);
            }
            catch (Exception e)
            {
                throw new ConversionException(e);
            }
        }
    }

    /**
     * Get the formatter for the given class/ locale. looks in the ConverterRegistry if a
     * IConverter was stored for the type of the property that implements IFormatter (as
     * well as IConverter).
     * @param clazz class of property
     * @param locale locale to get IFormatter for
     * @return IFormatter instance of IFormatter if found, null otherwise
     */
    private IFormatter getFormatter(Class clazz, Locale locale)
    {
        return getFormatter(null, null, clazz, locale);
    }

    /**
     * Get the formatter for the given name/ pattern/ class/ locale. 1. look in the
     * ConverterRegistry if a formatter was stored with the formatterName and optionally
     * locale as key. 2. if not found, look in the ConverterRegistry if a formatter was
     * stored with the pattern and optionally the locale as key. 3. if not found, look in
     * the ConverterRegistry if a IConverter was stored for the type of the property that
     * implements IFormatter (as well as IConverter).
     * @param formatterName name of formatter
     * @param pattern pattern: might be used as a key to store a IFormatter
     * @param clazz class of property
     * @param locale locale to get IFormatter for
     * @return IFormatter instance of IFormatter if found, null otherwise
     */
    private IFormatter getFormatter(String formatterName, String pattern, Class clazz, Locale locale)
    {
        IFormatter formatter = null;

        // first look up on fieldname
        if (formatterName != null)
        {
            formatter = this.converterRegistry.lookup(formatterName);
        }

        if ((formatter == null) && (pattern != null)) // not found, try pattern
        {
            formatter = this.converterRegistry.lookup(pattern);
        }

        IConverter converter = null;

        if ((formatter == null) && (clazz != null)) // not found, try converter
        {
            try
            {
                converter = this.converterRegistry.lookup(clazz, locale);
            }
            catch (NoSuchMethodException e)
            {
                throw new WicketRuntimeException(e);
            }
            catch (InstantiationException e)
            {
                throw new WicketRuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new WicketRuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new WicketRuntimeException(e);
            }

            if ((converter != null) && (converter instanceof IFormatter))
            {
                formatter = (IFormatter) converter;
            } // else will return null
        }

        return formatter;
    }

    /**
     * Formats the given object using the given locale.
     * @param object the un-formatted object
     * @param locale locale
     * @return value formatted
     */
    public String getObjectFormatted(Object object, Locale locale)
    {
        return getObjectFormatted(object, locale, null, null);
    }

    /**
     * Formats the given object using the given locale, formatter name and formatter
     * pattern.
     * @param object the un-formatted object
     * @param locale locale
     * @param formatterName name of formatter
     * @param formatPattern pattern to be used for formatting
     * @return value formatted
     */
    public String getObjectFormatted(Object object, Locale locale, String formatterName,
            String formatPattern)
    {
        if (object == null)
        {
            return null;
        }

        String formatted = null;
        IFormatter formatter = getFormatter(formatterName, formatPattern, object.getClass(), locale);

        if (formatter != null)
        {
            formatted = formatter.format(object, formatPattern);
        }
        else
        {
            formatted = convertToString(object);
        }

        return formatted;
    }
}
