//
//  FLRNCountryFromJSAdapter.m
//  Fidel
//
//  Created by Corneliu on 24/03/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "FLRNCountryFromJSAdapter.h"
#import "ConvertCountry.h"

@implementation FLRNCountryFromJSAdapter

- (NSDictionary *)constantsToExport {
    return FLCountryValues;
}

-(FLCountry)adaptedCountry:(id)rawCountry {
    return [ConvertCountry FLCountry:rawCountry];
}

@end
