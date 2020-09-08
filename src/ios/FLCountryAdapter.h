#import <Foundation/Foundation.h>
#import "FLRNConstantsProvider.h"
#if __has_include(<Fidel/Fidel-Swift.h>)
#import <Fidel/Fidel-Swift.h>
#elif __has_include("Fidel-Swift.h")
#import "Fidel-Swift.h"
#else
#import "Fidel/Fidel-Swift.h" // Required when used as a Pod in a Swift project
#endif

@protocol FLCountryAdapter <FLRNConstantsProvider>

-(FLCountry)adaptedCountry:(id)rawCountry;

@end
