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

package bisq.core.app.misc;

import bisq.core.alert.AlertModule;
import bisq.core.app.AppOptionKeys;
import bisq.core.app.BisqEnvironment;
import bisq.core.app.TorSetup;
import bisq.core.arbitration.ArbitratorModule;
import bisq.core.btc.BitcoinModule;
import bisq.core.dao.DaoModule;
import bisq.core.filter.FilterModule;
import bisq.core.network.p2p.seed.DefaultSeedNodeRepository;
import bisq.core.offer.OfferModule;
import bisq.core.proto.network.CoreNetworkProtoResolver;
import bisq.core.proto.persistable.CorePersistenceProtoResolver;
import bisq.core.trade.TradeModule;
import bisq.core.user.Preferences;
import bisq.core.user.User;

import bisq.network.crypto.EncryptionServiceModule;
import bisq.network.p2p.P2PModule;
import bisq.network.p2p.network.BridgeAddressProvider;
import bisq.network.p2p.seed.SeedNodeRepository;

import bisq.common.ClockWatcher;
import bisq.common.CommonOptionKeys;
import bisq.common.app.AppModule;
import bisq.common.crypto.KeyRing;
import bisq.common.crypto.KeyStorage;
import bisq.common.crypto.PubKeyRing;
import bisq.common.crypto.PubKeyRingProvider;
import bisq.common.proto.network.NetworkProtoResolver;
import bisq.common.proto.persistable.PersistenceProtoResolver;
import bisq.common.storage.Storage;

import org.springframework.core.env.Environment;

import com.google.inject.Singleton;
import com.google.inject.name.Names;

import java.io.File;

import static com.google.inject.name.Names.named;

public class ModuleForAppWithP2p extends AppModule {

    public ModuleForAppWithP2p(Environment environment) {
        super(environment);
    }

    @Override
    protected void configure() {
        configEnvironment();

        bind(KeyStorage.class).in(Singleton.class);
        bind(KeyRing.class).in(Singleton.class);
        bind(User.class).in(Singleton.class);
        bind(ClockWatcher.class).in(Singleton.class);
        bind(NetworkProtoResolver.class).to(CoreNetworkProtoResolver.class).in(Singleton.class);
        bind(PersistenceProtoResolver.class).to(CorePersistenceProtoResolver.class).in(Singleton.class);
        bind(Preferences.class).in(Singleton.class);
        bind(BridgeAddressProvider.class).to(Preferences.class).in(Singleton.class);
        bind(TorSetup.class).in(Singleton.class);

        bind(SeedNodeRepository.class).to(DefaultSeedNodeRepository.class).in(Singleton.class);

        File storageDir = new File(environment.getRequiredProperty(Storage.STORAGE_DIR));
        bind(File.class).annotatedWith(named(Storage.STORAGE_DIR)).toInstance(storageDir);

        File keyStorageDir = new File(environment.getRequiredProperty(KeyStorage.KEY_STORAGE_DIR));
        bind(File.class).annotatedWith(named(KeyStorage.KEY_STORAGE_DIR)).toInstance(keyStorageDir);

        Boolean useDevPrivilegeKeys = environment.getProperty(AppOptionKeys.USE_DEV_PRIVILEGE_KEYS, Boolean.class, false);
        bind(boolean.class).annotatedWith(Names.named(AppOptionKeys.USE_DEV_PRIVILEGE_KEYS)).toInstance(useDevPrivilegeKeys);

        Boolean useDevMode = environment.getProperty(CommonOptionKeys.USE_DEV_MODE, Boolean.class, false);
        bind(boolean.class).annotatedWith(Names.named(CommonOptionKeys.USE_DEV_MODE)).toInstance(useDevMode);

        String referralId = environment.getProperty(AppOptionKeys.REFERRAL_ID, String.class, "");
        bind(String.class).annotatedWith(Names.named(AppOptionKeys.REFERRAL_ID)).toInstance(referralId);

        // ordering is used for shut down sequence
        install(tradeModule());
        install(encryptionServiceModule());
        install(arbitratorModule());
        install(offerModule());
        install(p2pModule());
        install(bitcoinModule());
        install(daoModule());
        install(alertModule());
        install(filterModule());
        bind(PubKeyRing.class).toProvider(PubKeyRingProvider.class);

    }

    protected void configEnvironment() {
        bind(BisqEnvironment.class).toInstance((BisqEnvironment) environment);
    }

    protected TradeModule tradeModule() {
        return new TradeModule(environment);
    }

    protected EncryptionServiceModule encryptionServiceModule() {
        return new EncryptionServiceModule(environment);
    }

    protected ArbitratorModule arbitratorModule() {
        return new ArbitratorModule(environment);
    }

    protected AlertModule alertModule() {
        return new AlertModule(environment);
    }

    protected FilterModule filterModule() {
        return new FilterModule(environment);
    }

    protected OfferModule offerModule() {
        return new OfferModule(environment);
    }

    protected P2PModule p2pModule() {
        return new P2PModule(environment);
    }

    protected BitcoinModule bitcoinModule() {
        return new BitcoinModule(environment);
    }

    protected DaoModule daoModule() {
        return new DaoModule(environment);
    }
}
