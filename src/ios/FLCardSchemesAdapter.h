#import <Foundation/Foundation.h>
#import "FLRNConstantsProvider.h"

@protocol FLCardSchemesAdapter <FLRNConstantsProvider>

-(NSSet<NSNumber *> *)cardSchemesWithRawObject:(id)objectToAdapt;

@end
