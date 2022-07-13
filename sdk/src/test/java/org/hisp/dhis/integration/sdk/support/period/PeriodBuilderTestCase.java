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
package org.hisp.dhis.integration.sdk.support.period;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class PeriodBuilderTestCase
{
    @Test
    public void testDailyOf()
    {
        assertEquals( "20220712", PeriodBuilder.dayOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testWeekOf()
    {
        assertEquals( "2022W28", PeriodBuilder.weekOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testMonthOf()
    {
        assertEquals( "202207", PeriodBuilder.monthOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testBiMonthOf()
    {
        assertEquals( "202204B", PeriodBuilder.biMonthOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testSixMonthOf()
    {
        assertEquals( "2022S2", PeriodBuilder.sixMonthOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testFinancialYearStartingNovOf()
    {
        assertEquals( "2022Nov", PeriodBuilder.financialYearStartingNovOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testYearOf()
    {
        assertEquals( "2022", PeriodBuilder.yearOf( new Date( 1657626227255L ) ) );
    }

    @Test
    public void testYearOfWithOffset()
    {
        assertEquals( "2021", PeriodBuilder.yearOf( new Date( 1657626227255L ), -1 ) );
    }
}
