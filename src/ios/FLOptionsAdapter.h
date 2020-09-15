#import <Foundation/Foundation.h>

@protocol FLCountryAdapter;
@protocol FLCardSchemesAdapter;

@interface FLOptionsAdapter : NSObject

@property (nonatomic, readonly) NSDictionary *constantsToExport;

-(instancetype)initWithcardSchemesAdapter:(id<FLCardSchemesAdapter>)cardSchemesAdapter;

-(void)setOptions: (NSDictionary *)options;

@end
