/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.asset.coins;

import bisq.asset.AbstractAssetTest;

import org.junit.Test;

public class UnifyTest extends AbstractAssetTest {

    public UnifyTest() {
        super(new Unify.Mainnet());
    }

    @Test
    public void testValidAddresses() {
        assertValidAddress("UdBPZ2zHpB2Ut3RbxC6dtvtWyczbtSpDTx");
        assertValidAddress("UTSviz3KJKeY6yvQWscrT5DoNWQhm2Vsur");
        assertValidAddress("URMrjFwjkhNUW23ydWmiWRuEWjx6fEiUHz");
        assertValidAddress("UjCT1QoZqN2BRqEG7QdcZcGKuyZDtojjPn");
    }

    @Test
    public void testInvalidAddresses() {
        assertInvalidAddress("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYhemqq");
        assertInvalidAddress("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYheO");
        assertInvalidAddress("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYhek#");
    }
}