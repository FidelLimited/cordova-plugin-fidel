//
//  FLRNCountryFromJSAdapter.m
//  Fidel
//
//  Created by Corneliu on 24/03/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "FLRNCountryFromJSAdapter.h"


@implementation FLRNCountryFromJSAdapter

-(FLCountry)adaptedCountry:(id)rawCountry {
    NSNumber *rawValue = rawCountry;
    FLCountry convertedCountry = (FLCountry) rawValue.intValue;
    if (convertedCountry == 0) {
        return FLCountryUnitedKingdom;
    }
    else if (convertedCountry == 1) {
        return FLCountryIreland;
    }
    else if (convertedCountry == 2) {
        return FLCountryUnitedStates;
    }
    else if (convertedCountry == 3) {
        return FLCountrySweden;
    }
    else if (convertedCountry == 4) {
        return FLCountryJapan;
    }
    else if (convertedCountry == 5) {
        return FLCountryCanada;
    }
    else {
        return FLCountryNoDefault;
    }
}

@end
