/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.api.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "nextPage", "page", "pageCount", "pageSize", "prevPage", "total" } )
public class Pager
{
    @JsonProperty( "nextPage" )
    private String nextPage;

    @JsonProperty( "page" )
    private Integer page;

    @JsonProperty( "pageCount" )
    private Integer pageCount;

    @JsonProperty( "pageSize" )
    private Integer pageSize;

    @JsonProperty( "prevPage" )
    private String prevPage;

    @JsonProperty( "total" )
    private Integer total;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap();

    protected static final Object NOT_FOUND_VALUE = new Object();

    public Pager()
    {
    }

    public Pager( Pager source )
    {
        this.nextPage = source.nextPage;
        this.page = source.page;
        this.pageCount = source.pageCount;
        this.pageSize = source.pageSize;
        this.prevPage = source.prevPage;
        this.total = source.total;
    }

    public Pager( String nextPage, Integer page, Integer pageCount, Integer pageSize, String prevPage, Integer total )
    {
        this.nextPage = nextPage;
        this.page = page;
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        this.prevPage = prevPage;
        this.total = total;
    }

    @JsonProperty( "nextPage" )
    public String getNextPage()
    {
        return this.nextPage;
    }

    @JsonProperty( "nextPage" )
    public void setNextPage( String nextPage )
    {
        this.nextPage = nextPage;
    }

    public Pager withNextPage( String nextPage )
    {
        this.nextPage = nextPage;
        return this;
    }

    @JsonProperty( "page" )
    public Integer getPage()
    {
        return this.page;
    }

    @JsonProperty( "page" )
    public void setPage( Integer page )
    {
        this.page = page;
    }

    public Pager withPage( Integer page )
    {
        this.page = page;
        return this;
    }

    @JsonProperty( "pageCount" )
    public Integer getPageCount()
    {
        return this.pageCount;
    }

    @JsonProperty( "pageCount" )
    public void setPageCount( Integer pageCount )
    {
        this.pageCount = pageCount;
    }

    public Pager withPageCount( Integer pageCount )
    {
        this.pageCount = pageCount;
        return this;
    }

    @JsonProperty( "pageSize" )
    public Integer getPageSize()
    {
        return this.pageSize;
    }

    @JsonProperty( "pageSize" )
    public void setPageSize( Integer pageSize )
    {
        this.pageSize = pageSize;
    }

    public Pager withPageSize( Integer pageSize )
    {
        this.pageSize = pageSize;
        return this;
    }

    @JsonProperty( "prevPage" )
    public String getPrevPage()
    {
        return this.prevPage;
    }

    @JsonProperty( "prevPage" )
    public void setPrevPage( String prevPage )
    {
        this.prevPage = prevPage;
    }

    public Pager withPrevPage( String prevPage )
    {
        this.prevPage = prevPage;
        return this;
    }

    @JsonProperty( "total" )
    public Integer getTotal()
    {
        return this.total;
    }

    @JsonProperty( "total" )
    public void setTotal( Integer total )
    {
        this.total = total;
    }

    public Pager withTotal( Integer total )
    {
        this.total = total;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties()
    {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty( String name, Object value )
    {
        this.additionalProperties.put( name, value );
    }

    public Pager withAdditionalProperty( String name, Object value )
    {
        this.additionalProperties.put( name, value );
        return this;
    }

    protected boolean declaredProperty( String name, Object value )
    {
        switch ( name )
        {
        case "nextPage":
            if ( value instanceof String )
            {
                this.setNextPage( (String) value );
                return true;
            }

            throw new IllegalArgumentException(
                "property \"nextPage\" is of type \"java.lang.String\", but got " + value.getClass().toString() );
        case "page":
            if ( value instanceof Integer )
            {
                this.setPage( (Integer) value );
                return true;
            }

            throw new IllegalArgumentException(
                "property \"page\" is of type \"java.lang.Integer\", but got " + value.getClass().toString() );
        case "pageCount":
            if ( value instanceof Integer )
            {
                this.setPageCount( (Integer) value );
                return true;
            }

            throw new IllegalArgumentException(
                "property \"pageCount\" is of type \"java.lang.Integer\", but got " + value.getClass().toString() );
        case "pageSize":
            if ( value instanceof Integer )
            {
                this.setPageSize( (Integer) value );
                return true;
            }

            throw new IllegalArgumentException(
                "property \"pageSize\" is of type \"java.lang.Integer\", but got " + value.getClass().toString() );
        case "prevPage":
            if ( value instanceof String )
            {
                this.setPrevPage( (String) value );
                return true;
            }

            throw new IllegalArgumentException(
                "property \"prevPage\" is of type \"java.lang.String\", but got " + value.getClass().toString() );
        case "total":
            if ( value instanceof Integer )
            {
                this.setTotal( (Integer) value );
                return true;
            }

            throw new IllegalArgumentException(
                "property \"total\" is of type \"java.lang.Integer\", but got " + value.getClass().toString() );
        default:
            return false;
        }
    }

    protected Object declaredPropertyOrNotFound( String name, Object notFoundValue )
    {
        switch ( name )
        {
        case "nextPage":
            return this.getNextPage();
        case "page":
            return this.getPage();
        case "pageCount":
            return this.getPageCount();
        case "pageSize":
            return this.getPageSize();
        case "prevPage":
            return this.getPrevPage();
        case "total":
            return this.getTotal();
        default:
            return notFoundValue;
        }
    }

    public <T> T get( String name )
    {
        Object value = this.declaredPropertyOrNotFound( name, NOT_FOUND_VALUE );
        return (T) (NOT_FOUND_VALUE != value ? value : this.getAdditionalProperties().get( name ));
    }

    public void set( String name, Object value )
    {
        if ( !this.declaredProperty( name, value ) )
        {
            this.getAdditionalProperties().put( name, value );
        }

    }

    public Pager with( String name, Object value )
    {
        if ( !this.declaredProperty( name, value ) )
        {
            this.getAdditionalProperties().put( name, value );
        }

        return this;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( Pager.class.getName() ).append( '@' )
            .append( Integer.toHexString( System.identityHashCode( this ) ) ).append( '[' );
        sb.append( "nextPage" );
        sb.append( '=' );
        sb.append( this.nextPage == null ? "<null>" : this.nextPage );
        sb.append( ',' );
        sb.append( "page" );
        sb.append( '=' );
        sb.append( this.page == null ? "<null>" : this.page );
        sb.append( ',' );
        sb.append( "pageCount" );
        sb.append( '=' );
        sb.append( this.pageCount == null ? "<null>" : this.pageCount );
        sb.append( ',' );
        sb.append( "pageSize" );
        sb.append( '=' );
        sb.append( this.pageSize == null ? "<null>" : this.pageSize );
        sb.append( ',' );
        sb.append( "prevPage" );
        sb.append( '=' );
        sb.append( this.prevPage == null ? "<null>" : this.prevPage );
        sb.append( ',' );
        sb.append( "total" );
        sb.append( '=' );
        sb.append( this.total == null ? "<null>" : this.total );
        sb.append( ',' );
        sb.append( "additionalProperties" );
        sb.append( '=' );
        sb.append( this.additionalProperties == null ? "<null>" : this.additionalProperties );
        sb.append( ',' );
        if ( sb.charAt( sb.length() - 1 ) == ',' )
        {
            sb.setCharAt( sb.length() - 1, ']' );
        }
        else
        {
            sb.append( ']' );
        }

        return sb.toString();
    }

    public int hashCode()
    {
        int result = 1;
        result = result * 31 + (this.pageCount == null ? 0 : this.pageCount.hashCode());
        result = result * 31 + (this.total == null ? 0 : this.total.hashCode());
        result = result * 31 + (this.nextPage == null ? 0 : this.nextPage.hashCode());
        result = result * 31 + (this.pageSize == null ? 0 : this.pageSize.hashCode());
        result = result * 31 + (this.prevPage == null ? 0 : this.prevPage.hashCode());
        result = result * 31 + (this.page == null ? 0 : this.page.hashCode());
        result = result * 31 + (this.additionalProperties == null ? 0 : this.additionalProperties.hashCode());
        return result;
    }

    public boolean equals( Object other )
    {
        if ( other == this )
        {
            return true;
        }
        else if ( !(other instanceof Pager) )
        {
            return false;
        }
        else
        {
            Pager rhs = (Pager) other;
            return (this.pageCount == rhs.pageCount || this.pageCount != null && this.pageCount.equals( rhs.pageCount ))
                && (this.total == rhs.total || this.total != null && this.total.equals( rhs.total ))
                && (this.nextPage == rhs.nextPage || this.nextPage != null && this.nextPage.equals( rhs.nextPage ))
                && (this.pageSize == rhs.pageSize || this.pageSize != null && this.pageSize.equals( rhs.pageSize ))
                && (this.prevPage == rhs.prevPage || this.prevPage != null && this.prevPage.equals( rhs.prevPage ))
                && (this.page == rhs.page || this.page != null && this.page.equals( rhs.page ))
                && (this.additionalProperties == rhs.additionalProperties || this.additionalProperties != null
                    && this.additionalProperties.equals( rhs.additionalProperties ));
        }
    }
}