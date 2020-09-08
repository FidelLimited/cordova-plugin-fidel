#import "FLRNCardSchemesFromJSAdapter.h"
#if __has_include(<Fidel/Fidel-Swift.h>)
#import <Fidel/Fidel-Swift.h>
#elif __has_include("Fidel-Swift.h")
#import "Fidel-Swift.h"
#else
#import "Fidel/Fidel-Swift.h" // Required when used as a Pod in a Swift project
#endif

@implementation FLRNCardSchemesFromJSAdapter

-(NSSet<NSNumber *> *)cardSchemesWithRawObject:(id)objectToAdapt {
    NSMutableSet<NSNumber *> *setToReturn;
    if ([objectToAdapt isKindOfClass:[NSArray class]]) {
        NSArray *arrayToAdapt = (NSArray *)objectToAdapt;
        setToReturn = [NSMutableSet set];
        for (id objectToAdapt in arrayToAdapt) {
            NSNumber *rawValue = objectToAdapt;
            FLCardScheme convertedCardScheme = (FLCardScheme) rawValue.intValue;
            if (convertedCardScheme == 0) {
                FLCardScheme cardScheme = FLCardSchemeVisa;
                [setToReturn addObject:@(cardScheme)];
            }
            else if (convertedCardScheme == 1) {
                FLCardScheme cardScheme = FLCardSchemeMastercard;
                [setToReturn addObject:@(cardScheme)];
            }
            else if (convertedCardScheme == 2) {
                FLCardScheme cardScheme = FLCardSchemeAmericanExpress;
                [setToReturn addObject:@(cardScheme)];
            }
        }
    }
    return setToReturn;
}

@end
