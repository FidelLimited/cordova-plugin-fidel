#import <Foundation/Foundation.h>

@protocol FLCardSchemesAdapter

-(NSSet<NSNumber *> *)cardSchemesWithRawObject:(id)objectToAdapt;

@end
